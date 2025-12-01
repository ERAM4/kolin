package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.teacherstore.ui.screens.LoginScreen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_elementsAreDisplayed() {
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                LoginScreen(navController = navController)
            }
        }

        // --- CORRECCIÓN AQUÍ ---

        // 1. Verificar el TÍTULO (Tiene texto "Iniciar Sesión" Y NO se puede clickear)
        composeTestRule.onNode(
            hasText("Iniciar Sesión") and hasNoClickAction()
        ).assertIsDisplayed()

        // 2. Verificar el BOTÓN (Tiene texto "Iniciar Sesión" Y SÍ se puede clickear)
        // Nota: En tu código el botón dice "Iniciar Sesión" o "ENTRAR"?
        // Si cambiaste el texto a "ENTRAR" en pasos anteriores, usa "ENTRAR".
        // Si sigue diciendo "Iniciar Sesión", usa este bloque:
        composeTestRule.onNode(
            hasText("Iniciar Sesión") and hasClickAction()
        ).assertIsDisplayed()

        // 3. Verificar campos de texto
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun loginScreen_canInputText() {
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                LoginScreen(navController = navController)
            }
        }

        // Escribir en el correo
        composeTestRule.onNodeWithText("Correo Electrónico")
            .performTextInput("gamer@test.com")

        // Escribir en la contraseña
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // Hacer clic en el botón (Buscándolo específicamente como botón)
        composeTestRule.onNode(
            hasText("Iniciar Sesión") and hasClickAction()
        ).performClick()
    }
}