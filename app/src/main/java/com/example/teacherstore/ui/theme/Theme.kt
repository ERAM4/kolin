package com.example.teacherstore.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Definimos el esquema de colores GAMER
// Usamos las variables que definimos en Color.kt (Asegúrate de tenerlas allá)
private val GamerColorScheme = darkColorScheme(

    // El color principal (Botones, Checkboxes activos, etc.)
    primary = NeonCyan,
    onPrimary = Color.Black, // Texto negro sobre botón Cyan para contraste

    // El color secundario (Botones flotantes, detalles)
    secondary = NeonPurple,
    onSecondary = Color.White,

    // Fondos
    background = GamerBackground, // El azul oscuro profundo
    onBackground = TextWhite,     // Texto blanco sobre el fondo

    // Superficies (Tarjetas, BottomSheet, Menús)
    surface = GamerSurface,       // El gris azulado
    onSurface = TextWhite,

    // Errores
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun TeacherStoreTheme(
    // Ignoramos el ajuste del sistema, siempre queremos oscuro
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivamos el color dinámico para mantener nuestra identidad visual
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Aquí forzamos a que SIEMPRE use nuestro esquema Gamer
    val colorScheme = GamerColorScheme

    // Configuración para pintar la barra de estado (donde va la hora)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Pinta la barra del color del fondo para que se vea "infinita"
            window.statusBarColor = GamerBackground.toArgb()

            // Le dice al sistema: "El fondo es oscuro, pon los iconos (hora/batería) en blanco"
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de que Typography esté definido en Typography.kt
        content = content
    )
}