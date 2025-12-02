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

        // 2. Verificar Header (LOBBY)
        composeTestRule.onNodeWithText("LOBBY", substring = true).assertIsDisplayed()

        // 3. Verificar Barra de Búsqueda
        composeTestRule.onNodeWithText("Buscar", substring = true).assertIsDisplayed()

        // 4. Verificar Sección de Novedades
        composeTestRule.onNodeWithText("NOVEDADES", substring = true).assertIsDisplayed()

        // 5. Verificar Oferta Flash
        // Hacemos scroll hacia abajo para encontrarlo
        composeTestRule.onNodeWithText("OFERTA FLASH")
            .performScrollTo()
            .assertIsDisplayed()

        // 6. Verificar Título "MÁS VENDIDOS"
        // Este scroll baja la pantalla hasta el final
        composeTestRule.onNodeWithText("VENDIDOS", substring = true)
            .performScrollTo()
            .assertIsDisplayed()

        // 7. Verificar Producto "Teclado"
        // --- CORRECCIÓN AQUÍ ---
        // Quitamos .performScrollTo() porque ya bajamos con el título anterior.
        // Al estar dentro de una lista horizontal anidada, intentar scrollear al ítem suele fallar.
        // Solo verificamos que se vea.
        composeTestRule.onNodeWithText("Teclado", substring = true)
            .assertIsDisplayed()
    }
}