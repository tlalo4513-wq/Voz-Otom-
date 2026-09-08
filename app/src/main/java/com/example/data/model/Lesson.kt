package com.example.data.model

data class Lesson(
    val id: String,
    val title: String,
    val unit: String,
    val category: String,
    val level: String, // Principiante, Básico, Intermedio
    val description: String,
    val xpReward: Int = 50,
    val exercises: List<Exercise>
)

sealed class Exercise {
    data class FlashcardIntro(
        val otomiWord: String,
        val spanishTranslation: String,
        val phoneticGuide: String,
        val tip: String,
        val exampleSentence: String
    ) : Exercise()

    data class MultipleChoice(
        val question: String,
        val promptAudioWord: String = "",
        val options: List<String>,
        val correctIndex: Int,
        val explanation: String
    ) : Exercise()

    data class WordMatch(
        val prompt: String,
        val pairs: List<Pair<String, String>> // Otomi to Spanish pairs
    ) : Exercise()

    data class SentenceBuilder(
        val promptSpanish: String,
        val scrambledOtomiTokens: List<String>,
        val correctOtomiSentence: String,
        val explanation: String
    ) : Exercise()

    data class PronunciationExercise(
        val otomiWord: String,
        val spanishTranslation: String,
        val phoneticHint: String,
        val culturalContext: String
    ) : Exercise()
}
