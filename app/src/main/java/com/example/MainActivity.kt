package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.OtomiTopBar
import com.example.ui.screens.DictionaryScreen
import com.example.ui.screens.LessonPlayerScreen
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.MiniGameScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.PronunciationScreen
import com.example.ui.screens.TranslatorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OtomiTerracotta
import com.example.viewmodel.OtomiTab
import com.example.viewmodel.OtomiViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OtomiApp()
            }
        }
    }
}

@Composable
fun OtomiApp(viewModel: OtomiViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProgress by viewModel.userProgress.collectAsStateWithLifecycle()
    val activeLesson by viewModel.activeLesson.collectAsStateWithLifecycle()

    // If an interactive lesson is active, display full screen player
    if (activeLesson != null) {
        LessonPlayerScreen(
            viewModel = viewModel,
            lesson = activeLesson!!
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("otomi_main_scaffold"),
            topBar = {
                OtomiTopBar(
                    progress = userProgress,
                    title = when (currentTab) {
                        OtomiTab.LECCIONES -> "Aprende Otomí (Hñähñu)"
                        OtomiTab.PRONUNCIACION -> "Estudio de Pronunciación"
                        OtomiTab.TRADUCTOR -> "Traductor Español ⇄ Otomí"
                        OtomiTab.DICCIONARIO -> "Diccionario Hñähñu"
                        OtomiTab.MINIJUEGOS -> "Juegos & Progreso"
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("otomi_bottom_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == OtomiTab.LECCIONES,
                        onClick = { viewModel.currentTab.value = OtomiTab.LECCIONES },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Lecciones",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Lecciones", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OtomiTerracotta,
                            selectedTextColor = OtomiTerracotta,
                            indicatorColor = OtomiTerracotta.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("tab_lecciones")
                    )

                    NavigationBarItem(
                        selected = currentTab == OtomiTab.PRONUNCIACION,
                        onClick = { viewModel.currentTab.value = OtomiTab.PRONUNCIACION },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "Pronunciación",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Pronunciar", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OtomiTerracotta,
                            selectedTextColor = OtomiTerracotta,
                            indicatorColor = OtomiTerracotta.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("tab_pronunciacion")
                    )

                    NavigationBarItem(
                        selected = currentTab == OtomiTab.TRADUCTOR,
                        onClick = { viewModel.currentTab.value = OtomiTab.TRADUCTOR },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Traductor",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Traductor", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OtomiTerracotta,
                            selectedTextColor = OtomiTerracotta,
                            indicatorColor = OtomiTerracotta.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("tab_traductor")
                    )

                    NavigationBarItem(
                        selected = currentTab == OtomiTab.DICCIONARIO,
                        onClick = { viewModel.currentTab.value = OtomiTab.DICCIONARIO },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Diccionario",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Diccionario", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OtomiTerracotta,
                            selectedTextColor = OtomiTerracotta,
                            indicatorColor = OtomiTerracotta.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("tab_diccionario")
                    )

                    NavigationBarItem(
                        selected = currentTab == OtomiTab.MINIJUEGOS,
                        onClick = { viewModel.currentTab.value = OtomiTab.MINIJUEGOS },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Juegos y Progreso",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Progreso", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OtomiTerracotta,
                            selectedTextColor = OtomiTerracotta,
                            indicatorColor = OtomiTerracotta.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("tab_juegos")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    OtomiTab.LECCIONES -> LessonsScreen(
                        lessons = viewModel.lessons,
                        userProgress = userProgress,
                        onStartLesson = { lesson -> viewModel.startLesson(lesson) }
                    )
                    OtomiTab.PRONUNCIACION -> PronunciationScreen(viewModel = viewModel)
                    OtomiTab.TRADUCTOR -> TranslatorScreen(viewModel = viewModel)
                    OtomiTab.DICCIONARIO -> DictionaryScreen(viewModel = viewModel)
                    OtomiTab.MINIJUEGOS -> {
                        // Shows Games and allows switching to Progress
                        MiniGameScreen(viewModel = viewModel, progress = userProgress)
                    }
                }
            }
        }
    }
}

