package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val otomi: String,
    val spanish: String,
    val phoneticIpa: String,
    val phoneticSpanish: String,
    val category: String, // Saludos, Números, Familia, Animales, Colores, Alimentos, Cuerpo, Frases
    val exampleOtomi: String,
    val exampleSpanish: String,
    val culturalNote: String = "",
    val toneType: String = "Normal", // Normal, Nasal, Glotal, Aspirado
    val isBookmarked: Boolean = false
)

enum class OtomiCategory(val displayName: String, val iconName: String, val description: String) {
    SALUDOS("Saludos y Cortesía", "Handshake", "Aprende a saludar, agradecer y presentarte"),
    NUMEROS("Números", "Calculate", "Aprende a contar del 1 al 20 y más"),
    FAMILIA("Familia y Personas", "People", "Parientes, relaciones y personas"),
    ANIMALES("Animales de la Región", "Pets", "Fauna típica y doméstica del Valle del Mezquital"),
    COLORES("Colores y Naturaleza", "Palette", "Colores, elementos naturales y el paisaje"),
    ALIMENTOS("Alimentos y Cocina", "Restaurant", "Comida tradicional, ingredientes y cocina"),
    CUERPO("Cuerpo Humano", "AccessibilityNew", "Partes del cuerpo y sentidos"),
    FRASES("Frases Cotidianas", "Forum", "Expresiones prácticas para conversar")
}
