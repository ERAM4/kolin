package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.teacherstore.ui.screens.RegistroContent // Importamos el Content, no el Screen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registroScreen_componentsAreDisplayed() {
        // 1. Cargamos el CONTENIDO VISUAL (Sin ViewModel real)
        composeTestRule.setContent {
            TeacherStoreTheme {
                RegistroContent(
                    isLoading = false,
                    onNavigateLogin = {},
                    onRegistrar = { _, _, _, _ -> } // Lambda vacía
                )
            }
        }

        // 2. Verificar Título
        composeTestRule.onNodeWithText("CREAR CUENTA").assertIsDisplayed()

        // 3. Verificar Campos de Texto
        composeTestRule.onNodeWithText("Gamertag (Usuario)").assertIsDisplayed()

        // ¡OJO! Agregamos este campo en el paso anterior, el test debe verificarlo
        composeTestRule.onNodeWithText("Código Postal").assertIsDisplayed()

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        composeTestRule.onNodeWithText("Confirmar Contraseña")
            .performScrollTo()
            .assertIsDisplayed()

        // 4. Verificar Checkbox y Botón
        // Nota: En tu código pusimos "Acepto los términos", no "y condiciones"
        composeTestRule.onNodeWithText("Acepto los términos")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("REGISTRARSE")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun registroScreen_fillFormAndSubmit() {
        // Variable para verificar si el botón funcionó
        var seHizoClick = false

        composeTestRule.setContent {
            TeacherStoreTheme {
                RegistroContent(
                    isLoading = false,
                    onNavigateLogin = {},
                    onRegistrar = { nombre, cp, email, pass ->
                        // Si llega aquí, es que la validación pasó
                        seHizoClick = true
                    }
                )
            }
        }

        // 1. Llenar Nombre
        composeTestRule.onNodeWithText("Gamertag (Usuario)")
            .performTextInput("NuevoGamer123")

        // 2. Llenar Código Postal (NUEVO)
        // Si no llenamos esto, la validación fallará y el botón no hará nada
        composeTestRule.onNodeWithText("Código Postal")
            .performTextInput("99999")

        // 3. Llenar Correo
        composeTestRule.onNodeWithText("Email")
            .performTextInput("gamer@prueba.com")

        // 4. Llenar Contraseña
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("123456")

        // 5. Confirmar Contraseña
        composeTestRule.onNodeWithText("Confirmar Contraseña")
            .performScrollTo()
            .performTextInput("123456")

        // 6. Marcar el Checkbox
        composeTestRule.onNodeWithText("Acepto los términos")
            .performScrollTo()
            .performClick()

        // 7. Hacer clic en Registrarse
        composeTestRule.onNodeWithText("REGISTRARSE")
            .performScrollTo()
            .performClick()

        // 8. Verificación opcional:
        // Si la variable seHizoClick es true, significa que el formulario validó bien
        assert(seHizoClick)
    }
}