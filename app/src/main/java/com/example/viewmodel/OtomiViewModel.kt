package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.OtomiAudioSynthesizer
import com.example.audio.VoiceRecorderHelper
import com.example.data.OtomiRepository
import com.example.data.TranslationResult
import com.example.data.local.OtomiDatabase
import com.example.data.local.PrepopulatedData
import com.example.data.model.Achievement
import com.example.data.model.Exercise
import com.example.data.model.Lesson
import com.example.data.model.UserProgress
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OtomiViewModel(application: Application) : AndroidViewModel(application) {
    private val database = OtomiDatabase.getDatabase(application, viewModelScope)
    val repository = OtomiRepository(database)
    val audioSynthesizer = OtomiAudioSynthesizer(application)
    val recorderHelper = VoiceRecorderHelper(application)

    // Current navigation tab
    val currentTab = MutableStateFlow(OtomiTab.LECCIONES)

    // User Progress
    val userProgress: StateFlow<UserProgress> = repository.userProgress
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProgress(id = 1, xp = 50, streakDays = 1)
        )

    // Dictionary states
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Todas")
    val onlyBookmarks = MutableStateFlow(false)

    val allWords: StateFlow<List<VocabularyWord>> = repository.allWords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredWords: StateFlow<List<VocabularyWord>> = combine(
        allWords,
        searchQuery,
        selectedCategory,
        onlyBookmarks
    ) { words, query, cat, bookmarkedOnly ->
        words.filter { word ->
            val matchesQuery = query.isBlank() ||
                word.otomi.contains(query, ignoreCase = true) ||
                word.spanish.contains(query, ignoreCase = true) ||
                word.phoneticSpanish.contains(query, ignoreCase = true)

            val matchesCat = cat == "Todas" || word.category.equals(cat, ignoreCase = true)
            val matchesBookmark = !bookmarkedOnly || word.isBookmarked

            matchesQuery && matchesCat && matchesBookmark
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Translator states
    val translatorInput = MutableStateFlow("")
    val isSpanishToOtomi = MutableStateFlow(true)
    val translationResult = MutableStateFlow<TranslationResult?>(null)
    val isTranslating = MutableStateFlow(false)

    // Lessons
    val lessons: List<Lesson> = repository.getLessons()
    val activeLesson = MutableStateFlow<Lesson?>(null)
    val currentExerciseIndex = MutableStateFlow(0)
    val selectedOptionIndex = MutableStateFlow<Int?>(null)
    val isAnswerSubmitted = MutableStateFlow(false)
    val isAnswerCorrect = MutableStateFlow(false)
    val builtSentenceTokens = MutableStateFlow<List<String>>(emptyList())
    val lessonCompletedDialog = MutableStateFlow(false)

    // Pronunciation Studio
    val selectedPronunciationWord = MutableStateFlow<VocabularyWord?>(null)
    val isRecordingVoice = MutableStateFlow(false)
    val isPlayingUserVoice = MutableStateFlow(false)
    val pronunciationScore = MutableStateFlow<Int?>(null)
    val isAudioSpeaking = MutableStateFlow(false)

    // MiniGame states
    val gameScore = MutableStateFlow(0)
    val gameCombo = MutableStateFlow(1)
    val gameLives = MutableStateFlow(3)
    val gameTimeLeftSeconds = MutableStateFlow(30)
    val isGameActive = MutableStateFlow(false)
    val isGameOver = MutableStateFlow(false)
    val gameMode = MutableStateFlow(GameMode.SPEED_QUIZ)
    val currentQuizQuestion = MutableStateFlow<GameQuizItem?>(null)
    val memoryPairs = MutableStateFlow<List<MemoryCard>>(emptyList())
    val selectedFirstMemoryCard = MutableStateFlow<MemoryCard?>(null)
    private var gameTimerJob: Job? = null

    init {
        // Pre-populate if needed
        viewModelScope.launch {
            OtomiDatabase.populateInitialDatabase(database)
        }
    }

    // Audio Playback
    fun speakWord(word: String, phonetic: String = "") {
        isAudioSpeaking.value = true
        audioSynthesizer.speakWord(word, phonetic) {
            isAudioSpeaking.value = false
        }
    }

    // Bookmark Toggle
    fun toggleBookmark(word: VocabularyWord) {
        viewModelScope.launch {
            repository.toggleBookmark(word.id, word.isBookmarked)
        }
    }

    // Translator
    fun translate() {
        val input = translatorInput.value
        if (input.isBlank()) {
            translationResult.value = null
            return
        }
        viewModelScope.launch {
            isTranslating.value = true
            delay(150) // Micro feedback
            val result = repository.translate(input, isSpanishToOtomi.value)
            translationResult.value = result
            isTranslating.value = false
        }
    }

    fun swapTranslationDirection() {
        isSpanishToOtomi.value = !isSpanishToOtomi.value
        val currentTranslated = translationResult.value?.translatedText ?: ""
        if (currentTranslated.isNotBlank()) {
            translatorInput.value = currentTranslated
            translate()
        }
    }

    // Lesson Operations
    fun startLesson(lesson: Lesson) {
        activeLesson.value = lesson
        currentExerciseIndex.value = 0
        resetExerciseState()
    }

    fun closeLesson() {
        activeLesson.value = null
        lessonCompletedDialog.value = false
        resetExerciseState()
    }

    private fun resetExerciseState() {
        selectedOptionIndex.value = null
        isAnswerSubmitted.value = false
        isAnswerCorrect.value = false
        builtSentenceTokens.value = emptyList()
    }

    fun selectOption(index: Int) {
        if (!isAnswerSubmitted.value) {
            selectedOptionIndex.value = index
        }
    }

    fun toggleSentenceToken(token: String) {
        if (isAnswerSubmitted.value) return
        val current = builtSentenceTokens.value.toMutableList()
        if (current.contains(token)) {
            current.remove(token)
        } else {
            current.add(token)
        }
        builtSentenceTokens.value = current
    }

    fun submitExerciseAnswer() {
        val lesson = activeLesson.value ?: return
        val currentEx = lesson.exercises.getOrNull(currentExerciseIndex.value) ?: return

        when (currentEx) {
            is Exercise.MultipleChoice -> {
                val selected = selectedOptionIndex.value ?: return
                val correct = selected == currentEx.correctIndex
                isAnswerCorrect.value = correct
                isAnswerSubmitted.value = true
                if (correct) {
                    audioSynthesizer.playAcousticTone("Haxäi")
                }
            }
            is Exercise.SentenceBuilder -> {
                val built = builtSentenceTokens.value.joinToString(" ")
                val correct = built.trim().equals(currentEx.correctOtomiSentence.trim(), ignoreCase = true)
                isAnswerCorrect.value = correct
                isAnswerSubmitted.value = true
                if (correct) {
                    audioSynthesizer.playAcousticTone("Haxäi")
                }
            }
            is Exercise.FlashcardIntro -> {
                // Auto advances on Next
                isAnswerCorrect.value = true
                isAnswerSubmitted.value = true
            }
            is Exercise.WordMatch -> {
                isAnswerCorrect.value = true
                isAnswerSubmitted.value = true
            }
            is Exercise.PronunciationExercise -> {
                isAnswerCorrect.value = true
                isAnswerSubmitted.value = true
            }
        }
    }

    fun nextExercise() {
        val lesson = activeLesson.value ?: return
        val nextIdx = currentExerciseIndex.value + 1
        if (nextIdx < lesson.exercises.size) {
            currentExerciseIndex.value = nextIdx
            resetExerciseState()
        } else {
            // Lesson Finished
            viewModelScope.launch {
                repository.completeLesson(lesson.id, lesson.xpReward)
            }
            lessonCompletedDialog.value = true
        }
    }

    // Pronunciation Recording
    fun toggleRecording(word: VocabularyWord) {
        if (isRecordingVoice.value) {
            // Stop recording
            val score = recorderHelper.stopRecording()
            isRecordingVoice.value = false
            pronunciationScore.value = score
            viewModelScope.launch {
                repository.recordPronunciationPractice(additionalXp = 20)
            }
        } else {
            // Start recording
            pronunciationScore.value = null
            recorderHelper.startRecording(
                onStarted = { isRecordingVoice.value = true },
                onError = { isRecordingVoice.value = false }
            )
        }
    }

    fun playUserVoice() {
        if (isPlayingUserVoice.value) {
            recorderHelper.stopPlaying()
            isPlayingUserVoice.value = false
        } else {
            isPlayingUserVoice.value = true
            recorderHelper.playRecording {
                isPlayingUserVoice.value = false
            }
        }
    }

    // MiniGame Logic
    fun startMiniGame(mode: GameMode) {
        gameMode.value = mode
        gameScore.value = 0
        gameCombo.value = 1
        gameLives.value = 3
        isGameOver.value = false
        isGameActive.value = true

        if (mode == GameMode.SPEED_QUIZ) {
            gameTimeLeftSeconds.value = 45
            prepareNextQuizQuestion()
            startTimer()
        } else {
            gameTimeLeftSeconds.value = 60
            prepareMemoryGame()
            startTimer()
        }
    }

    private fun startTimer() {
        gameTimerJob?.cancel()
        gameTimerJob = viewModelScope.launch {
            while (gameTimeLeftSeconds.value > 0 && isGameActive.value && !isGameOver.value) {
                delay(1000)
                gameTimeLeftSeconds.value -= 1
            }
            if (gameTimeLeftSeconds.value <= 0 && isGameActive.value) {
                endMiniGame()
            }
        }
    }

    fun prepareNextQuizQuestion() {
        val vocab = allWords.value.ifEmpty { PrepopulatedData.getInitialVocabulary() }
        if (vocab.size < 4) return
        val target = vocab.random()
        val distractors = vocab.filter { it.id != target.id }.shuffled().take(3)
        val options = (distractors.map { it.spanish } + target.spanish).shuffled()
        currentQuizQuestion.value = GameQuizItem(
            otomiPrompt = target.otomi,
            phoneticHint = target.phoneticSpanish,
            correctSpanish = target.spanish,
            options = options
        )
    }

    fun submitQuizAnswer(selectedAnswer: String) {
        val question = currentQuizQuestion.value ?: return
        if (selectedAnswer == question.correctSpanish) {
            // Correct
            val points = 20 * gameCombo.value
            gameScore.value += points
            gameCombo.value = (gameCombo.value + 1).coerceAtMost(5)
            audioSynthesizer.playAcousticTone("Haxäi")
            prepareNextQuizQuestion()
        } else {
            // Wrong
            gameCombo.value = 1
            val remainingLives = gameLives.value - 1
            gameLives.value = remainingLives
            if (remainingLives <= 0) {
                endMiniGame()
            } else {
                prepareNextQuizQuestion()
            }
        }
    }

    private fun prepareMemoryGame() {
        val vocab = allWords.value.ifEmpty { PrepopulatedData.getInitialVocabulary() }.shuffled().take(6)
        val cards = mutableListOf<MemoryCard>()
        vocab.forEachIndexed { index, word ->
            cards.add(MemoryCard(id = index * 2, pairKey = word.otomi, display = word.otomi, isOtomi = true))
            cards.add(MemoryCard(id = index * 2 + 1, pairKey = word.otomi, display = word.spanish, isOtomi = false))
        }
        memoryPairs.value = cards.shuffled()
        selectedFirstMemoryCard.value = null
    }

    fun selectMemoryCard(card: MemoryCard) {
        if (card.isMatched || card.isFlipped) return
        val currentCards = memoryPairs.value.toMutableList()
        val first = selectedFirstMemoryCard.value

        if (first == null) {
            // Flip first card
            val idx = currentCards.indexOfFirst { it.id == card.id }
            if (idx != -1) {
                currentCards[idx] = card.copy(isFlipped = true)
                memoryPairs.value = currentCards
                selectedFirstMemoryCard.value = currentCards[idx]
            }
        } else {
            // Second card selected
            val idx = currentCards.indexOfFirst { it.id == card.id }
            if (idx != -1) {
                currentCards[idx] = card.copy(isFlipped = true)
                memoryPairs.value = currentCards

                if (first.pairKey == card.pairKey) {
                    // Match found!
                    val firstIdx = currentCards.indexOfFirst { it.id == first.id }
                    currentCards[firstIdx] = currentCards[firstIdx].copy(isMatched = true)
                    currentCards[idx] = currentCards[idx].copy(isMatched = true)
                    memoryPairs.value = currentCards
                    selectedFirstMemoryCard.value = null
                    gameScore.value += 30 * gameCombo.value
                    gameCombo.value = (gameCombo.value + 1).coerceAtMost(5)
                    audioSynthesizer.playAcousticTone("Haxäi")

                    // Check if all matched
                    if (currentCards.all { it.isMatched }) {
                        gameScore.value += 100 // completion bonus
                        endMiniGame()
                    }
                } else {
                    // Mismatch - flip back after delay
                    viewModelScope.launch {
                        delay(600)
                        val resetList = memoryPairs.value.toMutableList()
                        val fIdx = resetList.indexOfFirst { it.id == first.id }
                        val sIdx = resetList.indexOfFirst { it.id == card.id }
                        if (fIdx != -1) resetList[fIdx] = resetList[fIdx].copy(isFlipped = false)
                        if (sIdx != -1) resetList[sIdx] = resetList[sIdx].copy(isFlipped = false)
                        memoryPairs.value = resetList
                        selectedFirstMemoryCard.value = null
                    }
                }
            }
        }
    }

    fun endMiniGame() {
        gameTimerJob?.cancel()
        isGameActive.value = false
        isGameOver.value = true
        val score = gameScore.value
        val xpEarned = (score / 2).coerceAtLeast(20)
        viewModelScope.launch {
            repository.recordGameScore(score, xpEarned)
        }
    }

    // Achievements calculation
    fun getAchievements(progress: UserProgress): List<Achievement> {
        val completedCount = progress.getCompletedLessonsList().size
        return listOf(
            Achievement(
                id = "first_step",
                title = "Primer Saludo",
                description = "Completa tu primera lección interactiva en Hñähñu",
                iconEmoji = "🌱",
                isUnlocked = completedCount >= 1,
                progress = (completedCount / 1f).coerceIn(0f, 1f)
            ),
            Achievement(
                id = "streak_master",
                title = "Fuego Sagrado",
                description = "Mantén una racha de estudio de al menos 1 día",
                iconEmoji = "🔥",
                isUnlocked = progress.streakDays >= 1,
                progress = 1f
            ),
            Achievement(
                id = "numbers_pro",
                title = "Maestro de Números",
                description = "Completa la lección de contar del 1 al 5",
                iconEmoji = "🔢",
                isUnlocked = progress.isLessonCompleted("lesson_2"),
                progress = if (progress.isLessonCompleted("lesson_2")) 1f else 0f
            ),
            Achievement(
                id = "pronunciation_hero",
                title = "Oído y Voz de Oro",
                description = "Practica pronunciación 3 o más veces con grabación",
                iconEmoji = "🎙️",
                isUnlocked = progress.pronunciationPracticesCount >= 3,
                progress = (progress.pronunciationPracticesCount / 3f).coerceIn(0f, 1f)
            ),
            Achievement(
                id = "game_champion",
                title = "Ágil Hñähñu",
                description = "Alcanza 100 puntos en cualquier minijuego de vocabulario",
                iconEmoji = "⚡",
                isUnlocked = progress.miniGameHighScore >= 100,
                progress = (progress.miniGameHighScore / 100f).coerceIn(0f, 1f)
            ),
            Achievement(
                id = "polyglot",
                title = "Guardián de la Lengua",
                description = "Acumula más de 300 puntos de experiencia XP",
                iconEmoji = "👑",
                isUnlocked = progress.xp >= 300,
                progress = (progress.xp / 300f).coerceIn(0f, 1f)
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        audioSynthesizer.shutdown()
        recorderHelper.cleanup()
        gameTimerJob?.cancel()
    }
}

enum class OtomiTab(val label: String) {
    LECCIONES("Lecciones"),
    PRONUNCIACION("Pronunciar"),
    TRADUCTOR("Traductor"),
    DICCIONARIO("Diccionario"),
    MINIJUEGOS("Juegos & Progreso")
}

enum class GameMode {
    SPEED_QUIZ,
    MEMORY_CARDS
}

data class GameQuizItem(
    val otomiPrompt: String,
    val phoneticHint: String,
    val correctSpanish: String,
    val options: List<String>
)

data class MemoryCard(
    val id: Int,
    val pairKey: String,
    val display: String,
    val isOtomi: Boolean,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)
