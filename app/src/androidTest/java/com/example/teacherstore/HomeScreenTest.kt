package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import com.example.teacherstore.ui.screens.HomeScreen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsAllGamerSections() {
        // 1. Cargamos la pantalla
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                HomeScreen(navController = navController)
            }
        }

        // 2. Verificar Header (TopBar)
        composeTestRule.onNodeWithText("LOBBY").assertIsDisplayed()

        // --- CAMBIO: Ya no buscamos "HOLA, GAMER", buscamos la Barra de Búsqueda ---
        composeTestRule.onNodeWithText("Buscar juegos, equipo...").assertIsDisplayed()

        // 3. Verificar Sección de Novedades
        composeTestRule.onNodeWithText("NOVEDADES").assertIsDisplayed()

        // Verificamos que al menos una noticia se ve
        composeTestRule.onNodeWithText("ELDEN RING: DLC").assertIsDisplayed()

        // 4. Verificar Oferta Flash (Haciendo Scroll hacia abajo)
        composeTestRule.onNodeWithText("OFERTA FLASH")
            .performScrollTo() // El robot baja la pantalla
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Pack Streamer")
            .performScrollTo()
            .assertIsDisplayed()

        // 5. Verificar Sección de Tendencias (Más abajo)
        composeTestRule.onNodeWithText("MÁS VENDIDOS") // Antes se llamaba "TENDENCIAS"
            .performScrollTo()
            .assertIsDisplayed()

        // Verificamos un producto de tendencia
        composeTestRule.onNodeWithText("Teclado RGB")
            .performScrollTo()
            .assertIsDisplayed()
    }
}