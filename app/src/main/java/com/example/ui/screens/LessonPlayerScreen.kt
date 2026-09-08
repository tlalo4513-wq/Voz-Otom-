package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Exercise
import com.example.data.model.Lesson
import com.example.ui.theme.OtomiError
import com.example.ui.theme.OtomiGold
import com.example.ui.theme.OtomiSuccess
import com.example.ui.theme.OtomiTeal
import com.example.ui.theme.OtomiTerracotta
import com.example.viewmodel.OtomiViewModel

@Composable
fun LessonPlayerScreen(
    viewModel: OtomiViewModel,
    lesson: Lesson
) {
    val currentIdx by viewModel.currentExerciseIndex.collectAsStateWithLifecycle()
    val isSubmitted by viewModel.isAnswerSubmitted.collectAsStateWithLifecycle()
    val isCorrect by viewModel.isAnswerCorrect.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOptionIndex.collectAsStateWithLifecycle()
    val sentenceTokens by viewModel.builtSentenceTokens.collectAsStateWithLifecycle()
    val showCompletedDialog by viewModel.lessonCompletedDialog.collectAsStateWithLifecycle()

    val totalExercises = lesson.exercises.size
    val currentExercise = lesson.exercises.getOrNull(currentIdx)
    val progressFraction = if (totalExercises > 0) (currentIdx + 1).toFloat() / totalExercises else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.closeLesson() },
                    modifier = Modifier.testTag("close_lesson_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar lección",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = OtomiTerracotta,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Text(
                    text = "${currentIdx + 1}/$totalExercises",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exercise Body Content
            if (currentExercise != null) {
                when (currentExercise) {
                    is Exercise.FlashcardIntro -> {
                        FlashcardExerciseView(
                            exercise = currentExercise,
                            onSpeak = { viewModel.speakWord(currentExercise.otomiWord, currentExercise.phoneticGuide) }
                        )
                    }
                    is Exercise.MultipleChoice -> {
                        MultipleChoiceExerciseView(
                            exercise = currentExercise,
                            selectedIndex = selectedOption,
                            isSubmitted = isSubmitted,
                            onSelect = { viewModel.selectOption(it) },
                            onSpeak = { if (currentExercise.promptAudioWord.isNotBlank()) viewModel.speakWord(currentExercise.promptAudioWord) }
                        )
                    }
                    is Exercise.SentenceBuilder -> {
                        SentenceBuilderExerciseView(
                            exercise = currentExercise,
                            selectedTokens = sentenceTokens,
                            isSubmitted = isSubmitted,
                            onToggleToken = { viewModel.toggleSentenceToken(it) }
                        )
                    }
                    is Exercise.WordMatch -> {
                        WordMatchExerciseView(
                            exercise = currentExercise,
                            onSpeak = { viewModel.speakWord(it) }
                        )
                    }
                    is Exercise.PronunciationExercise -> {
                        PronunciationExerciseView(
                            exercise = currentExercise,
                            onSpeak = { viewModel.speakWord(currentExercise.otomiWord, currentExercise.phoneticHint) }
                        )
                    }
                }
            }
        }

        // Bottom Action / Feedback Dock
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = if (!isSubmitted) {
                MaterialTheme.colorScheme.surface
            } else if (isCorrect) {
                OtomiTeal.copy(alpha = 0.12f)
            } else {
                OtomiError.copy(alpha = 0.12f)
            },
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (isSubmitted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isCorrect) OtomiSuccess else OtomiError),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isCorrect) "¡Excelente! Nsadi xähmä" else "Sigue practicando",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (isCorrect) OtomiSuccess else OtomiError
                            )
                            val explanation = when (val ex = currentExercise) {
                                is Exercise.MultipleChoice -> ex.explanation
                                is Exercise.SentenceBuilder -> ex.explanation
                                else -> ""
                            }
                            if (explanation.isNotBlank()) {
                                Text(
                                    text = explanation,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Main Bottom Button
                if (!isSubmitted) {
                    val canCheck = when (currentExercise) {
                        is Exercise.FlashcardIntro -> true
                        is Exercise.MultipleChoice -> selectedOption != null
                        is Exercise.SentenceBuilder -> sentenceTokens.isNotEmpty()
                        is Exercise.WordMatch -> true
                        is Exercise.PronunciationExercise -> true
                        null -> false
                    }
                    Button(
                        onClick = { viewModel.submitExerciseAnswer() },
                        enabled = canCheck,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("check_answer_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = OtomiTerracotta),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (currentExercise is Exercise.FlashcardIntro) "Continuar" else "Comprobar respuesta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.nextExercise() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("next_exercise_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCorrect) OtomiTeal else OtomiTerracotta
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (currentIdx + 1 >= totalExercises) "Finalizar lección" else "Siguiente ejercicio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    // Completion Celebration Dialog
    if (showCompletedDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeLesson() },
            title = {
                Text(
                    text = "¡Lección Completada! 🎉",
                    fontWeight = FontWeight.Bold,
                    color = OtomiTerracotta,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Jamädi (Gracias) por tu dedicación a aprender el hñähñu.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = OtomiGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = OtomiGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+${lesson.xpReward} Puntos de Experiencia XP",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.closeLesson() },
                    colors = ButtonDefaults.buttonColors(containerColor = OtomiTerracotta),
                    modifier = Modifier.fillMaxWidth().testTag("finish_lesson_dialog_button")
                ) {
                    Text("Continuar mi camino", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun FlashcardExerciseView(
    exercise: Exercise.FlashcardIntro,
    onSpeak: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nuevo Vocabulario",
            color = OtomiTerracotta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, OtomiTerracotta.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = exercise.otomiWord,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = exercise.phoneticGuide,
                    fontSize = 15.sp,
                    color = OtomiTeal,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(OtomiTerracotta.copy(alpha = 0.12f))
                        .testTag("flashcard_audio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Escuchar pronunciación",
                        tint = OtomiTerracotta,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Significado en español:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = exercise.spanishTranslation,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = OtomiGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Consejo de pronunciación",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = exercise.tip,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (exercise.exampleSentence.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ejemplo: ${exercise.exampleSentence}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = OtomiTeal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceExerciseView(
    exercise: Exercise.MultipleChoice,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSelect: (Int) -> Unit,
    onSpeak: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pregunta de Comprensión",
            color = OtomiTerracotta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = exercise.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (exercise.promptAudioWord.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSpeak,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("audio_prompt_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = OtomiTerracotta
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escuchar '${exercise.promptAudioWord}'")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        exercise.options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val isCorrect = isSubmitted && index == exercise.correctIndex
            val isWrong = isSubmitted && isSelected && index != exercise.correctIndex

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(enabled = !isSubmitted) { onSelect(index) }
                    .border(
                        width = if (isSelected || isCorrect || isWrong) 2.dp else 1.dp,
                        color = when {
                            isCorrect -> OtomiSuccess
                            isWrong -> OtomiError
                            isSelected -> OtomiTerracotta
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("option_$index"),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isCorrect -> OtomiSuccess.copy(alpha = 0.12f)
                        isWrong -> OtomiError.copy(alpha = 0.12f)
                        isSelected -> OtomiTerracotta.copy(alpha = 0.08f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCorrect -> OtomiSuccess
                                    isWrong -> OtomiError
                                    isSelected -> OtomiTerracotta
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A'.code + index).toChar().toString(),
                            color = if (isSelected || isCorrect || isWrong) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderExerciseView(
    exercise: Exercise.SentenceBuilder,
    selectedTokens: List<String>,
    isSubmitted: Boolean,
    onToggleToken: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Construye la Frase",
            color = OtomiTerracotta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = exercise.promptSpanish,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sentence placement zone
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    RoundedCornerShape(16.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (selectedTokens.isEmpty()) {
                Text(
                    text = "Toca las palabras abajo para ordenarlas aquí...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedTokens.forEach { token ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OtomiTerracotta,
                            modifier = Modifier.clickable(enabled = !isSubmitted) { onToggleToken(token) }
                        ) {
                            Text(
                                text = token,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Palabras disponibles:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Scrambled pool
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            exercise.scrambledOtomiTokens.forEach { token ->
                val isUsed = selectedTokens.contains(token)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isUsed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (isUsed) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                    modifier = Modifier.clickable(enabled = !isSubmitted) { onToggleToken(token) }
                ) {
                    Text(
                        text = token,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isUsed) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WordMatchExerciseView(
    exercise: Exercise.WordMatch,
    onSpeak: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Empareja los Significados",
            color = OtomiTerracotta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = exercise.prompt,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        exercise.pairs.forEach { (otomi, spanish) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onSpeak(otomi) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = OtomiTerracotta,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = otomi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = spanish,
                        fontSize = 15.sp,
                        color = OtomiTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun PronunciationExerciseView(
    exercise: Exercise.PronunciationExercise,
    onSpeak: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Práctica de Pronunciación",
            color = OtomiTerracotta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = exercise.otomiWord,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = exercise.spanishTranslation,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(OtomiTerracotta.copy(alpha = 0.15f))
                        .testTag("pronounce_exercise_speak_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Escuchar",
                        tint = OtomiTerracotta,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Guía fonética: ${exercise.phoneticHint}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = OtomiTeal
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = exercise.culturalContext,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
