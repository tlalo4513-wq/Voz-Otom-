package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey
    val id: Int = 1,
    val xp: Int = 0,
    val streakDays: Int = 1,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val completedLessonsIds: String = "", // Comma-separated IDs
    val totalWordsLearned: Int = 0,
    val miniGameHighScore: Int = 0,
    val pronunciationPracticesCount: Int = 0
) {
    fun getCompletedLessonsList(): List<String> =
        if (completedLessonsIds.isBlank()) emptyList() else completedLessonsIds.split(",").map { it.trim() }

    fun isLessonCompleted(lessonId: String): Boolean =
        getCompletedLessonsList().contains(lessonId)

    fun getLevelTitle(): String = when {
        xp >= 1200 -> "Maestro Hñähñu (Hablante Fluido)"
        xp >= 700 -> "Guardián de la Lengua (Intermedio)"
        xp >= 300 -> "Explorador Otomí (En progreso)"
        xp >= 100 -> "Aprendiz Entusiasta (Básico)"
        else -> "Iniciador del Hñähñu (Principiante)"
    }

    fun getNextLevelTargetXp(): Int = when {
        xp < 100 -> 100
        xp < 300 -> 300
        xp < 700 -> 700
        xp < 1200 -> 1200
        else -> 2000
    }
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val progress: Float = 0f // 0.0 to 1.0
)
