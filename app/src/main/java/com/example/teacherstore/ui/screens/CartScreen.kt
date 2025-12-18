package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teacherstore.model.database.DataProduct
import com.example.teacherstore.viewmodel.ProductViewModel

@Composable
fun CartScreen(navController: NavController, productViewModel: ProductViewModel) {
    val context = LocalContext.current
    val productsList by productViewModel.allProductsInCart.collectAsState(initial = emptyList())

    CartContent(
        productsList = productsList,
        onBackClick = { navController.popBackStack() },
        onDeleteProduct = { productId -> productViewModel.deleteProductPerId(productId) },
        onCheckout = {
            // Lógica de Checkout: Borrar BD y Mostrar Mensaje
            productViewModel.clearCart()
            Toast.makeText(context, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    productsList: List<DataProduct>,
    onBackClick: () -> Unit,
    onDeleteProduct: (Int) -> Unit,
    onCheckout: () -> Unit // Recibe la acción
) {
    val groupedProducts = remember(productsList) { productsList.groupingBy { it.productName }.eachCount() }
    val distinctProducts = remember(productsList) { productsList.distinctBy { it.productName } }
    val totalAmount = productsList.sumOf { it.price }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MI LOOT", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.primary) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            if (productsList.isNotEmpty()) {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 16.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total a Pagar:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                            Text("$${String.format("%.2f", totalAmount)}", color = MaterialTheme.colorScheme.primary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        // Botón conectado a la acción onCheckout
                        Button(onClick = onCheckout, shape = RoundedCornerShape(12.dp), modifier = Modifier.height(50.dp)) {
                            Text("CHECKOUT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (productsList.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.surfaceVariant)
                Text("Inventario vacío", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(distinctProducts) { product ->
                    val quantity = groupedProducts[product.productName] ?: 1
                    CartItemCard(
                        product = product,
                        quantity = quantity,
                        onDeleteClick = {
                            val productsToDelete = productsList.filter { it.productName == product.productName }
                            productsToDelete.forEach { p -> onDeleteProduct(p.id) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemCard(product: DataProduct, quantity: Int, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl).crossfade(true).build(),
                contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(Color.Black)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.productName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$${product.price}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    if (quantity > 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(4.dp)) {
                            Text("x$quantity", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) }
        }
    }
}
//