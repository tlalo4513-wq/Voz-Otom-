package com.example.data

import com.example.data.local.OtomiDatabase
import com.example.data.local.PrepopulatedData
import com.example.data.model.Lesson
import com.example.data.model.UserProgress
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale

class OtomiRepository(private val database: OtomiDatabase) {
    private val vocabDao = database.vocabularyDao()
    private val progressDao = database.userProgressDao()

    val allWords: Flow<List<VocabularyWord>> = vocabDao.getAllWords()
    val bookmarkedWords: Flow<List<VocabularyWord>> = vocabDao.getBookmarkedWords()
    val userProgress: Flow<UserProgress?> = progressDao.getUserProgress()

    fun searchWords(query: String): Flow<List<VocabularyWord>> = vocabDao.searchWords(query)

    fun getWordsByCategory(category: String): Flow<List<VocabularyWord>> =
        vocabDao.getWordsByCategory(category)

    fun getLessons(): List<Lesson> = PrepopulatedData.getLessons()

    suspend fun toggleBookmark(wordId: Int, currentStatus: Boolean) {
        vocabDao.setBookmark(wordId, !currentStatus)
    }

    suspend fun completeLesson(lessonId: String, xpEarned: Int) {
        val currentProgress = progressDao.getUserProgressOnce() ?: UserProgress(id = 1)
        val completedList = currentProgress.getCompletedLessonsList().toMutableList()
        val isFirstTime = !completedList.contains(lessonId)
        if (isFirstTime) {
            completedList.add(lessonId)
        }
        val updated = currentProgress.copy(
            xp = currentProgress.xp + (if (isFirstTime) xpEarned else (xpEarned / 3).coerceAtLeast(10)),
            completedLessonsIds = completedList.joinToString(","),
            totalWordsLearned = (completedList.size * 5).coerceAtLeast(currentProgress.totalWordsLearned),
            lastActiveTimestamp = System.currentTimeMillis()
        )
        progressDao.insertOrUpdate(updated)
    }

    suspend fun recordPronunciationPractice(additionalXp: Int = 15) {
        val currentProgress = progressDao.getUserProgressOnce() ?: UserProgress(id = 1)
        val updated = currentProgress.copy(
            xp = currentProgress.xp + additionalXp,
            pronunciationPracticesCount = currentProgress.pronunciationPracticesCount + 1,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        progressDao.insertOrUpdate(updated)
    }

    suspend fun recordGameScore(score: Int, xpBonus: Int) {
        val currentProgress = progressDao.getUserProgressOnce() ?: UserProgress(id = 1)
        val newHigh = maxOf(currentProgress.miniGameHighScore, score)
        val updated = currentProgress.copy(
            xp = currentProgress.xp + xpBonus,
            miniGameHighScore = newHigh,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        progressDao.insertOrUpdate(updated)
    }

    /**
     * Bidirectional Otomi - Spanish translator engine.
     * Checks exact phrase match, token replacement, and vocabulary normalization.
     */
    suspend fun translate(inputText: String, isSpanishToOtomi: Boolean): TranslationResult {
        val trimmed = inputText.trim()
        if (trimmed.isBlank()) {
            return TranslationResult("", emptyList(), "Ingresa una palabra o frase para traducir.")
        }

        val allVocab = allWords.firstOrNull() ?: PrepopulatedData.getInitialVocabulary()
        val normalizedInput = trimmed.lowercase(Locale.getDefault())

        // 1. Direct match
        val directMatch = allVocab.find { word ->
            if (isSpanishToOtomi) {
                word.spanish.lowercase(Locale.getDefault()).contains(normalizedInput) ||
                normalizedInput.contains(word.spanish.lowercase(Locale.getDefault()))
            } else {
                word.otomi.lowercase(Locale.getDefault()).contains(normalizedInput) ||
                normalizedInput.contains(word.otomi.lowercase(Locale.getDefault()))
            }
        }

        if (directMatch != null) {
            val translated = if (isSpanishToOtomi) directMatch.otomi else directMatch.spanish
            return TranslationResult(
                translatedText = translated,
                matchedWords = listOf(directMatch),
                culturalNote = directMatch.culturalNote,
                phoneticGuide = directMatch.phoneticSpanish
            )
        }

        // 2. Token / word-by-word approximation
        val tokens = trimmed.split(Regex("[\\s,;!?.]+")).filter { it.isNotBlank() }
        val matches = mutableListOf<VocabularyWord>()
        val translatedTokens = tokens.map { token ->
            val cleanToken = token.lowercase(Locale.getDefault())
            val match = allVocab.find { word ->
                if (isSpanishToOtomi) {
                    word.spanish.lowercase(Locale.getDefault()).split(Regex("[\\s/()]+")).contains(cleanToken)
                } else {
                    word.otomi.lowercase(Locale.getDefault()).split(Regex("[\\s/()]+")).contains(cleanToken)
                }
            }
            if (match != null) {
                matches.add(match)
                if (isSpanishToOtomi) match.otomi else match.spanish
            } else {
                token // Keep original if unknown
            }
        }

        val resultStr = translatedTokens.joinToString(" ")
        val note = if (matches.isNotEmpty()) {
            "Traducción aproximada basada en el vocabulario Hñähñu disponible (${matches.size} términos identificados)."
        } else {
            "No se encontró una coincidencia exacta en el diccionario básico. Prueba con palabras como 'hola', 'gracias', 'madre', 'uno', 'tortilla', o consulta el Diccionario."
        }

        return TranslationResult(
            translatedText = resultStr,
            matchedWords = matches.distinctBy { it.id },
            culturalNote = note,
            phoneticGuide = matches.firstOrNull()?.phoneticSpanish ?: ""
        )
    }
}

data class TranslationResult(
    val translatedText: String,
    val matchedWords: List<VocabularyWord>,
    val culturalNote: String,
    val phoneticGuide: String = ""
)
