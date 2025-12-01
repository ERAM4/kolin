package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.teacherstore.model.product.Product
import com.example.teacherstore.ui.screens.CatalogContent
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class CatalogScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun catalogScreen_displaysProducts() {
        // 1. Creamos datos falsos para la prueba
        val fakeProducts = listOf(
            Product(1, "Mouse Test", "Descripción prueba", 50.0, ""),
            Product(2, "Teclado Test", "Descripción prueba", 100.0, ""),
            Product(3, "Monitor Test", "Descripción prueba", 200.0, "")
        )

        // 2. Cargamos SOLO el contenido visual (Sin ViewModel)
        composeTestRule.setContent {
            TeacherStoreTheme {
                CatalogContent(
                    products = fakeProducts,
                    onBackClick = {},
                    onCartClick = {},
                    onAddToCart = {}
                )
            }
        }

        // 3. Verificamos que aparezca el título
        composeTestRule.onNodeWithText("CATÁLOGO").assertIsDisplayed()

        // 4. Verificamos que aparezcan nuestros productos falsos
        composeTestRule.onNodeWithText("Mouse Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("Teclado Test").assertIsDisplayed()

        // Hacemos scroll para ver el último por si acaso
        composeTestRule.onNodeWithText("Monitor Test")
            .performScrollTo()
            .assertIsDisplayed()
    }
}