package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.UserProgress
import com.example.ui.theme.OtomiError
import com.example.ui.theme.OtomiGold
import com.example.ui.theme.OtomiSuccess
import com.example.ui.theme.OtomiTeal
import com.example.ui.theme.OtomiTerracotta
import com.example.viewmodel.GameMode
import com.example.viewmodel.MemoryCard
import com.example.viewmodel.OtomiViewModel

@Composable
fun MiniGameScreen(
    viewModel: OtomiViewModel,
    progress: UserProgress
) {
    val isGameActive by viewModel.isGameActive.collectAsStateWithLifecycle()
    val isGameOver by viewModel.isGameOver.collectAsStateWithLifecycle()
    val gameMode by viewModel.gameMode.collectAsStateWithLifecycle()
    val score by viewModel.gameScore.collectAsStateWithLifecycle()
    val combo by viewModel.gameCombo.collectAsStateWithLifecycle()
    val lives by viewModel.gameLives.collectAsStateWithLifecycle()
    val timeLeft by viewModel.gameTimeLeftSeconds.collectAsStateWithLifecycle()

    var selectedSubTab by remember { mutableIntStateOf(0) }

    if (!isGameActive && !isGameOver) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = {
                        Text(
                            "Minijuegos",
                            fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedSubTab == 0) OtomiTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = {
                        Text(
                            "Mi Progreso & Medallas",
                            fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedSubTab == 1) OtomiTerracotta else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            if (selectedSubTab == 0) {
                GameSelectionHub(
                    highScore = progress.miniGameHighScore,
                    onSelectMode = { mode -> viewModel.startMiniGame(mode) }
                )
            } else {
                ProgressScreen(viewModel = viewModel, progress = progress)
            }
        }
    } else {
        // Active Game Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .testTag("active_game_screen")
        ) {
            // Game HUD: Score, Combo, Timer, Lives
            GameHudHeader(
                score = score,
                combo = combo,
                lives = lives,
                timeLeft = timeLeft,
                gameMode = gameMode,
                onQuit = { viewModel.endMiniGame() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (gameMode == GameMode.SPEED_QUIZ) {
                SpeedQuizGameView(
                    viewModel = viewModel,
                    onSpeak = { word, hint -> viewModel.speakWord(word, hint) }
                )
            } else {
                MemoryCardsGameView(viewModel = viewModel)
            }
        }
    }

    // Game Over / Victory Dialog
    if (isGameOver) {
        val earnedXp = (score / 2).coerceAtLeast(20)
        AlertDialog(
            onDismissRequest = { viewModel.isGameOver.value = false },
            title = {
                Text(
                    text = if (lives > 0) "¡Juego Completado! 🏆" else "¡Fin del Juego! ⚡",
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
                        text = "Puntaje alcanzado: $score puntos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OtomiGold.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = OtomiGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+$earnedXp XP añadido a tu perfil",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.startMiniGame(gameMode) },
                    colors = ButtonDefaults.buttonColors(containerColor = OtomiTerracotta),
                    modifier = Modifier.testTag("play_again_button")
                ) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Jugar de nuevo")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.isGameOver.value = false }) {
                    Text("Menú de juegos")
                }
            }
        )
    }
}

@Composable
fun GameSelectionHub(
    highScore: Int,
    onSelectMode: (GameMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp, bottom = 96.dp)
            .testTag("game_selection_hub"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Minijuegos Hñähñu",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Refuerza el vocabulario adquirido de manera divertida y eficaz.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // High Score Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OtomiGold.copy(alpha = 0.12f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = OtomiGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Récord Personal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$highScore Puntos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mode 1 Card: Speed Arcade
        GameModeCard(
            title = "Atrapa la Palabra (Arcade Veloz)",
            description = "Pon a prueba tus reflejos. Traduce palabras en otomí contra reloj, mantén tu racha de combos y cuida tus 3 vidas.",
            icon = Icons.Default.Speed,
            badge = "Modo Rápido • 45s",
            accentColor = OtomiTerracotta,
            onPlay = { onSelectMode(GameMode.SPEED_QUIZ) },
            testTag = "start_speed_quiz_button"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mode 2 Card: Memory Match
        GameModeCard(
            title = "Parejas y Memoria Hñähñu",
            description = "Encuentra los pares de palabras en otomí y su traducción en español volteando tarjetas antes de que termine el tiempo.",
            icon = Icons.Default.GridOn,
            badge = "Memoria Visual • 60s",
            accentColor = OtomiTeal,
            onPlay = { onSelectMode(GameMode.MEMORY_CARDS) },
            testTag = "start_memory_game_button"
        )
    }
}

@Composable
fun GameModeCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String,
    accentColor: Color,
    onPlay: () -> Unit,
    testTag: String
) {
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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(26.dp))
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = badge,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPlay,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(testTag)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Jugar ahora", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GameHudHeader(
    score: Int,
    combo: Int,
    lives: Int,
    timeLeft: Int,
    gameMode: GameMode,
    onQuit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score & Combo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$score pts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (combo > 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = OtomiGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "x$combo Combo!",
                                fontWeight = FontWeight.Bold,
                                color = OtomiGold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Lives (for speed quiz)
                if (gameMode == GameMode.SPEED_QUIZ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Vida",
                                tint = if (index < lives) OtomiError else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(horizontal = 1.dp)
                            )
                        }
                    }
                }

                // Timer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (timeLeft <= 10) OtomiError else OtomiTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${timeLeft}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (timeLeft <= 10) OtomiError else MaterialTheme.colorScheme.onSurface
                    )
                }

                OutlinedButton(
                    onClick = onQuit,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Salir", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun SpeedQuizGameView(
    viewModel: OtomiViewModel,
    onSpeak: (String, String) -> Unit
) {
    val question by viewModel.currentQuizQuestion.collectAsStateWithLifecycle()

    if (question != null) {
        val q = question!!
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                        text = "¿Qué significa esta palabra?",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = q.otomiPrompt,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = q.phoneticHint,
                        fontSize = 14.sp,
                        color = OtomiTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    IconButton(
                        onClick = { onSpeak(q.otomiPrompt, q.phoneticHint) },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(OtomiTerracotta.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Escuchar",
                            tint = OtomiTerracotta,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Options Grid/Column
            q.options.forEachIndexed { index, option ->
                Button(
                    onClick = { viewModel.submitQuizAnswer(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .height(54.dp)
                        .testTag("quiz_choice_$index"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MemoryCardsGameView(viewModel: OtomiViewModel) {
    val cards by viewModel.memoryPairs.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Encuentra las parejas Otomí - Español",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(cards, key = { it.id }) { card ->
                MemoryCardItem(
                    card = card,
                    onTap = { viewModel.selectMemoryCard(card) }
                )
            }
        }
    }
}

@Composable
fun MemoryCardItem(
    card: MemoryCard,
    onTap: () -> Unit
) {
    val isRevealed = card.isFlipped || card.isMatched

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isRevealed) { onTap() }
            .testTag("memory_card_${card.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                card.isMatched -> OtomiSuccess.copy(alpha = 0.15f)
                card.isFlipped -> OtomiTerracotta.copy(alpha = 0.15f)
                else -> OtomiTeal
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isRevealed) 1.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isRevealed) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(
                        text = card.display,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = if (card.isMatched) OtomiSuccess else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (card.isOtomi) "Hñähñu" else "Español",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = "🌸",
                    fontSize = 28.sp
                )
            }
        }
    }
}
