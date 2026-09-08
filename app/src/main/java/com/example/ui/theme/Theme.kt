package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = OtomiDarkPrimary,
    onPrimary = Color(0xFF581403),
    primaryContainer = OtomiTerracottaDark,
    onPrimaryContainer = Color(0xFFFFDBD2),
    secondary = OtomiDarkTeal,
    onSecondary = Color(0xFF003732),
    secondaryContainer = OtomiTeal,
    onSecondaryContainer = OtomiTealContainer,
    tertiary = OtomiGoldLight,
    onTertiary = Color(0xFF452B00),
    background = OtomiDarkBg,
    onBackground = Color(0xFFE4E2DC),
    surface = OtomiDarkSurface,
    onSurface = Color(0xFFE4E2DC),
    surfaceVariant = OtomiDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC7C2BA),
    outline = Color(0xFF908C85)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = OtomiTerracotta,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD3),
    onPrimaryContainer = OtomiTerracottaDark,
    secondary = OtomiTeal,
    onSecondary = Color.White,
    secondaryContainer = OtomiTealContainer,
    onSecondaryContainer = Color(0xFF00201D),
    tertiary = OtomiGold,
    onTertiary = Color.White,
    tertiaryContainer = OtomiGoldContainer,
    onTertiaryContainer = Color(0xFF2C1600),
    background = OtomiLinen,
    onBackground = OtomiOnSurface,
    surface = OtomiSurface,
    onSurface = OtomiOnSurface,
    surfaceVariant = OtomiSurfaceVariant,
    onSurfaceVariant = Color(0xFF4D453E),
    outline = OtomiOutline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Use custom curated cultural palette by default for authentic identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

