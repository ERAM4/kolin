package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.teacherstore.ui.screens.MainScreen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_allElementsAreVisible() {
        // 1. Configuramos la pantalla
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                // Aquí MainScreen creará su propio ViewModel internamente.
                // Si tu ViewModel requiere dependencias complejas (BD), este test podría fallar.
                // Si eso pasa, avísame para enseñarte a "Mockear" el ViewModel.
                MainScreen(navController = navController)
            }
        }

        // 2. Verificamos los Textos Principales
        // El título del juego
        composeTestRule.onNodeWithText("LEVEL UP-GAMER").assertIsDisplayed()

        // El slogan
        composeTestRule.onNodeWithText("Tu tienda definitiva").assertIsDisplayed()

        // 3. Verificamos los Botones
        // Botón Iniciar Sesión (Buscamos texto + que sea clickeable)
        composeTestRule.onNode(
            hasText("INICIAR SESIÓN") and hasClickAction()
        ).assertIsDisplayed()

        // Botón Registrarse
        composeTestRule.onNode(
            hasText("REGISTRARSE") and hasClickAction()
        ).assertIsDisplayed()

        // Botón Ayuda
        composeTestRule.onNode(
            hasText("¿Necesitas ayuda o soporte?") and hasClickAction()
        ).assertIsDisplayed()
    }

    @Test
    fun mainScreen_buttonsAreClickable() {
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }

        // Simplemente verificamos que podemos hacer clic sin que la app explote
        composeTestRule.onNodeWithText("INICIAR SESIÓN").performClick()
        composeTestRule.onNodeWithText("REGISTRARSE").performClick()
    }
}