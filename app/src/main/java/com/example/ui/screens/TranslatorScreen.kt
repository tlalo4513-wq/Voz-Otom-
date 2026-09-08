package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.OtomiGold
import com.example.ui.theme.OtomiTeal
import com.example.ui.theme.OtomiTerracotta
import com.example.viewmodel.OtomiViewModel

@Composable
fun TranslatorScreen(viewModel: OtomiViewModel) {
    val input by viewModel.translatorInput.collectAsStateWithLifecycle()
    val isEsToOtomi by viewModel.isSpanishToOtomi.collectAsStateWithLifecycle()
    val translationResult by viewModel.translationResult.collectAsStateWithLifecycle()
    val isTranslating by viewModel.isTranslating.collectAsStateWithLifecycle()

    val quickPhrases = if (isEsToOtomi) {
        listOf("Hola", "Buenos días", "Gracias", "¿Cómo estás?", "Tortilla", "Mi mamá", "Caballo", "Uno", "Sí", "Adiós")
    } else {
        listOf("Kogui", "Haxäi", "Jamädi", "¿Hanja gi 'müi?", "Hme", "Mä me", "Faní", "N'da", "Hã", "Magö")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("translator_screen"),
        contentPadding = PaddingValues(16.dp, bottom = 96.dp)
    ) {
        item {
            // Direction Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "De:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isEsToOtomi) "Español (México)" else "Otomí (Hñähñu)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { viewModel.swapTranslationDirection() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(OtomiTerracotta.copy(alpha = 0.12f))
                            .testTag("swap_translation_direction_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Cambiar dirección",
                            tint = OtomiTerracotta,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "A:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isEsToOtomi) "Otomí (Hñähñu)" else "Español (México)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = OtomiTerracotta
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Text Input Box
            OutlinedTextField(
                value = input,
                onValueChange = {
                    viewModel.translatorInput.value = it
                    viewModel.translate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("translator_input_field"),
                placeholder = {
                    Text(
                        text = if (isEsToOtomi) "Escribe palabras en español (ej. buenos días, gracias, mamá)..." else "Escribe en otomí (ej. haxäi, jamädi, faní)..."
                    )
                },
                trailingIcon = {
                    if (input.isNotBlank()) {
                        IconButton(onClick = {
                            viewModel.translatorInput.value = ""
                            viewModel.translate()
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar texto")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 5
            )
        }

        // Quick phrase chips
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Sugerencias rápidas:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickPhrases) { phrase ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable {
                            viewModel.translatorInput.value = phrase
                            viewModel.translate()
                        }
                    ) {
                        Text(
                            text = phrase,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Translation Result Card
        item {
            Spacer(modifier = Modifier.height(20.dp))

            if (isTranslating) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OtomiTerracotta)
                }
            } else if (translationResult != null) {
                val result = translationResult!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Traducción",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OtomiTeal
                            )

                            if (result.translatedText.isNotBlank()) {
                                IconButton(
                                    onClick = { viewModel.speakWord(result.translatedText, result.phoneticGuide) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(OtomiTerracotta.copy(alpha = 0.1f))
                                        .testTag("translator_audio_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Escuchar traducción",
                                        tint = OtomiTerracotta,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = result.translatedText.ifBlank { "Sin coincidencia" },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (result.phoneticGuide.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pronunciación: ${result.phoneticGuide}",
                                fontSize = 14.sp,
                                color = OtomiTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (result.culturalNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = OtomiGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = result.culturalNote,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
