package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.teacherstore.ui.screens.RegistroScreen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registroScreen_componentsAreDisplayed() {
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                RegistroScreen(navController = navController)
            }
        }

        // 1. Verificar Título
        composeTestRule.onNodeWithText("CREAR CUENTA").assertIsDisplayed()

        // 2. Verificar Campos de Texto
        composeTestRule.onNodeWithText("Gamertag (Usuario)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        // A veces el campo de confirmar contraseña queda abajo si la pantalla es pequeña,
        // usamos performScrollTo() por si acaso.
        composeTestRule.onNodeWithText("Confirmar Contraseña")
            .performScrollTo()
            .assertIsDisplayed()

        // 3. Verificar Checkbox y Botón
        composeTestRule.onNodeWithText("Acepto los términos y condiciones").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("REGISTRARSE").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun registroScreen_fillFormAndSubmit() {
        composeTestRule.setContent {
            TeacherStoreTheme {
                val navController = rememberNavController()
                RegistroScreen(navController = navController)
            }
        }

        // 1. Llenar Nombre
        composeTestRule.onNodeWithText("Gamertag (Usuario)")
            .performTextInput("NuevoGamer123")

        // 2. Llenar Correo
        composeTestRule.onNodeWithText("Email")
            .performTextInput("gamer@prueba.com")

        // 3. Llenar Contraseña
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // 4. Confirmar Contraseña
        composeTestRule.onNodeWithText("Confirmar Contraseña")
            .performScrollTo() // Bajamos por si acaso
            .performTextInput("123456")

        // 5. Marcar el Checkbox (Términos)
        // Buscamos el texto del checkbox y le damos click.
        // Verificamos que estaba apagado y ahora está encendido (opcional pero buena práctica)
        val checkboxNode = composeTestRule.onNodeWithText("Acepto los términos y condiciones")

        checkboxNode.performScrollTo()
        // checkboxNode.assertIsOff() // Verifica que empieza desmarcado
        checkboxNode.performClick()
        // checkboxNode.assertIsOn()  // Verifica que se marcó

        // 6. Hacer clic en Registrarse
        composeTestRule.onNodeWithText("REGISTRARSE")
            .performScrollTo()
            .assertIsEnabled() // Verifica que el botón está habilitado
            .performClick()
    }
}