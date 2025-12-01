package com.example.teacherstore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.teacherstore.model.database.DataProduct
import com.example.teacherstore.ui.screens.CartContent
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cart_showsEmptyState_whenListIsEmpty() {
        // 1. Cargamos el carrito VACÍO
        composeTestRule.setContent {
            TeacherStoreTheme {
                CartContent(
                    productsList = emptyList(), // Lista vacía
                    onBackClick = {},
                    onDeleteProduct = {}
                )
            }
        }

        // 2. Verificamos el mensaje de vacío
        composeTestRule.onNodeWithText("MI LOOT").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inventario vacío").assertIsDisplayed()
    }

    @Test
    fun cart_calculatesTotalAndGroupsItems() {
        // 1. Creamos datos falsos:
        // - 2 Teclados ($50 c/u)
        // - 1 Mouse ($30)
        // TOTAL ESPERADO: $130.00
        val fakeProducts = listOf(
            DataProduct(1, "Teclado Gamer", 50.0, "Desc", ""),
            DataProduct(2, "Teclado Gamer", 50.0, "Desc", ""), // Repetido
            DataProduct(3, "Mouse Pro", 30.0, "Desc", "")
        )

        // 2. Cargamos el carrito CON datos
        composeTestRule.setContent {
            TeacherStoreTheme {
                CartContent(
                    productsList = fakeProducts,
                    onBackClick = {},
                    onDeleteProduct = {}
                )
            }
        }

        // 3. Verificamos que los productos se ven
        composeTestRule.onNodeWithText("Teclado Gamer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mouse Pro").assertIsDisplayed()

        // 4. Verificamos la agrupación (Stacking)
        // Debería aparecer "x2" al lado del teclado
        composeTestRule.onNodeWithText("x2").assertIsDisplayed()

        // 5. Verificamos el TOTAL MATEMÁTICO
        // 50 + 50 + 30 = 130.00
        composeTestRule.onNodeWithText("$130.00").assertIsDisplayed()

        // Verificamos botón de Checkout
        composeTestRule.onNodeWithText("CHECKOUT").assertIsDisplayed()
    }
}