package com.example.teacherstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teacherstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, productViewModel: ProductViewModel) {

    // Obtenemos los productos
    val products by productViewModel.allProductsInCart.collectAsState(initial = emptyList())

    // Calculamos el TOTAL a pagar (Sumando precios)
    val totalAmount = products.sumOf { it.price } // Asumiendo que 'price' es Double o Int

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Fondo Oscuro

        // BARRA SUPERIOR
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MI LOOT (CARRITO)",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary // Cyan
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },

        // BARRA INFERIOR (TOTAL Y PAGAR) - Solo se muestra si hay productos
        bottomBar = {
            if (products.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 16.dp, // Sombra para separar del contenido
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total a Pagar:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$${String.format("%.2f", totalAmount)}", // Formato con 2 decimales
                                color = MaterialTheme.colorScheme.primary, // Cyan
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { /* Lógica de pago aquí */ },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("CHECKOUT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

    ) { innerPadding ->

        // CONTENIDO
        if (products.isEmpty()) {
            // ESTADO VACÍO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu inventario está vacío",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "¡Ve al catálogo y busca nuevo equipo!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("IR AL CATÁLOGO")
                }
            }
        } else {
            // LISTA DE PRODUCTOS
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(products.size) { index ->
                    val product = products[index]

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface // Gris Oscuro
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // IMAGEN DEL PRODUCTO
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(product.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = product.productName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black) // Fondo por si falla la imagen
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // INFO DEL PRODUCTO
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = product.productName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$${product.price}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary // Morado
                                )
                            }

                            // BOTÓN ELIMINAR
                            IconButton(onClick = {
                                productViewModel.deleteProductPerId(product.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error // Rojo
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}