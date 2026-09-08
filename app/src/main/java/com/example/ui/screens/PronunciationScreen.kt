package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.PrepopulatedData
import com.example.data.model.VocabularyWord
import com.example.ui.theme.OtomiError
import com.example.ui.theme.OtomiGold
import com.example.ui.theme.OtomiSuccess
import com.example.ui.theme.OtomiTeal
import com.example.ui.theme.OtomiTerracotta
import com.example.viewmodel.OtomiViewModel

@Composable
fun PronunciationScreen(viewModel: OtomiViewModel) {
    val context = LocalContext.current
    val allWords by viewModel.allWords.collectAsStateWithLifecycle()
    val wordsList = if (allWords.isNotEmpty()) allWords else PrepopulatedData.getInitialVocabulary()

    val selectedWord by viewModel.selectedPronunciationWord.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecordingVoice.collectAsStateWithLifecycle()
    val isPlayingUser by viewModel.isPlayingUserVoice.collectAsStateWithLifecycle()
    val score by viewModel.pronunciationScore.collectAsStateWithLifecycle()

    val activeWord = selectedWord ?: wordsList.firstOrNull()

    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Estudio de Voz", "Guía Fonética Hñähñu", "Catálogo de Tonos")

    // Permission launcher for microphone
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && activeWord != null) {
            viewModel.toggleRecording(activeWord)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("pronunciation_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = tabTitle,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == index) OtomiTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }

        when (activeTab) {
            0 -> {
                // Interactive Voice Studio
                item {
                    activeWord?.let { word ->
                        PronunciationStudioCard(
                            word = word,
                            isRecording = isRecording,
                            isPlayingUser = isPlayingUser,
                            score = score,
                            onPlayModel = { viewModel.speakWord(word.otomi, word.phoneticSpanish) },
                            onToggleRecord = {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    viewModel.toggleRecording(word)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            onPlayUserVoice = { viewModel.playUserVoice() }
                        )
                    }
                }

                item {
                    Text(
                        text = "Selecciona una palabra para practicar",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                    )
                }

                items(wordsList.take(15)) { word ->
                    WordPronounceRow(
                        word = word,
                        isSelected = activeWord?.id == word.id,
                        onSelect = { viewModel.selectedPronunciationWord.value = word },
                        onListen = { viewModel.speakWord(word.otomi, word.phoneticSpanish) }
                    )
                }
            }

            1 -> {
                // Phonetic Guide (Vowels, Glottal, Aspirates)
                item {
                    PhoneticGuideSection(onPlaySound = { sound -> viewModel.speakWord(sound) })
                }
            }

            2 -> {
                // Tones & Accents
                item {
                    TonalCatalogSection(
                        words = wordsList,
                        onPlayWord = { word -> viewModel.speakWord(word.otomi, word.phoneticSpanish) }
                    )
                }
            }
        }
    }
}

@Composable
fun PronunciationStudioCard(
    word: VocabularyWord,
    isRecording: Boolean,
    isPlayingUser: Boolean,
    score: Int?,
    onPlayModel: () -> Unit,
    onToggleRecord: () -> Unit,
    onPlayUserVoice: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRecording) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_pulse"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = OtomiTerracotta.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "Tono: ${word.toneType}",
                    color = OtomiTerracotta,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.otomi,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = word.phoneticSpanish,
                fontSize = 16.sp,
                color = OtomiTeal,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "\"${word.spanish}\"",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action: Listen to Authentic Model
            OutlinedButton(
                onClick = onPlayModel,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(46.dp)
                    .testTag("listen_model_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = OtomiTerracotta
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escuchar modelo nativo", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recording Button & Waveform Area
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(if (isRecording) OtomiError else OtomiTerracotta)
                    .clickable { onToggleRecord() }
                    .testTag("record_voice_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (isRecording) "Detener grabación" else "Grabar mi voz",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isRecording) "Grabando tu voz... Toca para detener" else "Toca el micrófono y pronuncia la palabra",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isRecording) OtomiError else MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Result and Playback Section
            if (score != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (score >= 80) OtomiTeal.copy(alpha = 0.12f) else OtomiGold.copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (score >= 80) OtomiSuccess else OtomiGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Precisión de Pronunciación: $score%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = if (score >= 85) "¡Excelente ritmo y resonancia en la laringe! +20 XP" else "¡Buen intento! Continúa practicando la entonación.",
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = onPlayUserVoice,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlayingUser) OtomiError else OtomiTeal
                            ),
                            modifier = Modifier.testTag("play_user_recording_button")
                        ) {
                            Icon(
                                imageVector = if (isPlayingUser) Icons.Default.Stop else Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isPlayingUser) "Detener audio" else "Escuchar mi grabación")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordPronounceRow(
    word: VocabularyWord,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onListen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onSelect() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) OtomiTerracotta else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.otomi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${word.spanish} • ${word.phoneticSpanish}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onListen,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(OtomiTerracotta.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Escuchar",
                    tint = OtomiTerracotta,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun PhoneticGuideSection(onPlaySound: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Guía del Alfabeto y Fonemas Hñähñu",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "La lengua otomí cuenta con vocales orales y nasales, además del corte de glotis y consonantes aspiradas.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Nasal vowels
        PhoneticCard(
            title = "Vocales Nasales (Ä, Ë, Ö, Ü)",
            symbol = "ä, ë, ö, ü",
            explanation = "Se pronuncian dejando salir el aire simultáneamente por la boca y la cavidad nasal. Por ejemplo en 'Jamädi' (gracias) o 'Haxäi' (buenos días).",
            exampleWord = "Haxäi / Jamädi",
            onListen = { onPlaySound("Haxäi") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Glottal Stop
        PhoneticCard(
            title = "El Saltillo o Pausa Glotal (')",
            symbol = "' (apóstrofo)",
            explanation = "Indica una oclusión o corte instantáneo en las cuerdas vocales, como al decir 'oh-oh' en español. Esencial para distinguir palabras.",
            exampleWord = "N'da (Uno) / T'u (Hijo)",
            onListen = { onPlaySound("N'da") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Aspirates & Digraphs
        PhoneticCard(
            title = "Dígrafos Especiales (Hñ, Tz, X)",
            symbol = "hñ, tz, x",
            explanation = "'Hñ' es una consonante nasal sorda (como soplar suave por la nariz antes de una ñ). 'X' suena como la 'sh' en inglés.",
            exampleWord = "Hñu (Tres) / Xita (Abuelo)",
            onListen = { onPlaySound("Hñu") }
        )
    }
}

@Composable
fun PhoneticCard(
    title: String,
    symbol: String,
    explanation: String,
    exampleWord: String,
    onListen: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = OtomiTeal.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = symbol,
                        color = OtomiTeal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = explanation,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ejemplo: $exampleWord",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = OtomiTerracotta
                )
                OutlinedButton(
                    onClick = onListen,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Escuchar", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TonalCatalogSection(
    words: List<VocabularyWord>,
    onPlayWord: (VocabularyWord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Catálogo de Tonos y Variantes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "El hñähñu es una lengua tonal: la altura musical de una sílaba puede cambiar el sentido de una palabra.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        words.groupBy { it.toneType }.forEach { (tone, toneWords) ->
            Text(
                text = "Tono / Articulación: $tone (${toneWords.size} palabras)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = OtomiTerracotta,
                modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
            )

            toneWords.take(4).forEach { word ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = word.otomi, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = word.spanish, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onPlayWord(word) }) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = OtomiTerracotta
                            )
                        }
                    }
                }
            }
        }
    }
}
