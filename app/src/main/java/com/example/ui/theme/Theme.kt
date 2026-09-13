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

private val DarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = Color(0xFF1A1305),
    primaryContainer = Color(0xFF332711),
    onPrimaryContainer = ChampagneGoldLight,
    secondary = GenZLilac,
    onSecondary = Color(0xFF1E1430),
    secondaryContainer = Color(0xFF2E2442),
    onSecondaryContainer = Color(0xFFE3D4FC),
    tertiary = StreetwearTeal,
    onTertiary = Color(0xFF003831),
    background = ObsidianBg,
    onBackground = TextPrimaryDark,
    surface = ObsidianSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ObsidianBorder,
    outlineVariant = Color(0xFF3B4050)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF9E6E1A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCEECC),
    onPrimaryContainer = Color(0xFF2C1D02),
    secondary = Color(0xFF6B4FA1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEDBFF),
    onSecondaryContainer = Color(0xFF23143E),
    tertiary = Color(0xFF006A60),
    onTertiary = Color.White,
    background = CreamBg,
    onBackground = TextPrimaryLight,
    surface = CreamSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = CreamBorder,
    outlineVariant = Color(0xFFCDC2B3)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek fashion dark mode
    dynamicColor: Boolean = false, // Keep branded high-fashion luxury aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
