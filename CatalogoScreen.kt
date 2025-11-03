package com.example.teacherstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teacherstore.model.product.Product
import com.example.teacherstore.ui.theme.TeacherStoreTheme

/**
 * Clase de datos que representa un producto.
 * Es mejor si esta clase está en su propio archivo:
 * /model/product/Product.kt
 */
// data class Product(
//    val id: Int,
//    val name: String,
//    val description: String,
//    val price: Double,
//    val imageUrl: String
// )

/**
 * Composable que muestra un único producto en una tarjeta.
 * Incluye la imagen, nombre, descripción y precio.
 */
@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen del producto usando Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true) // Animación de fundido suave
                    .build(),
                contentDescription = "Imagen de ${product.name}",
                contentScale = ContentScale.Crop, // Escala la imagen para llenar el espacio
                modifier = Modifier
                    .size(80.dp) // Tamaño fijo para la imagen
                    .clip(RoundedCornerShape(8.dp)) // Bordes redondeados
            )

            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre imagen y texto

            // Columna con la información del producto
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2 // Limita la descripción a 2 líneas
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${"%.2f".format(product.price)}", // Formatea el precio a 2 decimales
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Composable para la pantalla principal del catálogo.
 * Muestra una lista de productos usando LazyColumn.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController) {
    // Lista de ejemplo. En una app real, esto vendría de un ViewModel.
    val productList = listOf(
        Product(1, "Lápiz Grafito", "Lápiz de madera para escribir y dibujar.", 0.50, "https://picsum.photos/id/101/200/200"),
        Product(2, "Cuaderno Universitario", "Cuaderno de 100 hojas con espiral metálico.", 2.99, "https://picsum.photos/id/122/200/200"),
        Product(3, "Goma de Borrar", "Goma de borrar suave que no daña el papel.", 0.75, "https://picsum.photos/id/40/200/200"),
        Product(4, "Regla 30cm", "Regla de plástico transparente, ideal para geometría.", 1.20, "https://picsum.photos/id/24/200/200"),
        Product(5, "Set de Marcadores", "Paquete con 12 marcadores de colores variados.", 5.50, "https://picsum.photos/id/175/200/200"),
        Product(6, "Mochila Escolar", "Mochila resistente con múltiples compartimentos.", 25.00, "https://picsum.photos/id/145/200/200")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Catálogo de Productos") })
        }
    ) { innerPadding ->
        // LazyColumn es eficiente para listas largas, ya que solo compone los elementos visibles. [2, 7]
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(productList) { product ->
                ProductItem(product = product)
            }
        }
    }
}

/**
 * Preview para ver cómo luce el ProductItem en Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    TeacherStoreTheme {
        val sampleProduct = Product(
            id = 1,
            name = "Producto de Muestra",
            description = "Esta es una descripción de ejemplo para el producto.",
            price = 9.99,
            imageUrl = "https://picsum.photos/id/1/200/200"
        )
        ProductItem(product = sampleProduct)
    }
}

/**
 * Preview para ver cómo luce la pantalla completa del Catálogo.
 */
@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    TeacherStoreTheme {
        // Para el preview, podemos pasar un NavController de prueba.
        CatalogScreen(navController = NavController(LocalContext.current))
    }
}


