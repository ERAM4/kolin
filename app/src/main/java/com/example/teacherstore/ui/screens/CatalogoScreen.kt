package com.example.teacherstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teacherstore.model.database.DataProduct
import com.example.teacherstore.model.product.Product
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.viewmodel.MainViewModel
import com.example.teacherstore.viewmodel.ProductViewModel

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    productViewModel: ProductViewModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${product.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    productViewModel.insertProduct(DataProduct(productName = product.name, price = product.price, description = product.description, imageUrl = product.imageUrl))
                }) {
                    Text("Añadir al carrito")
                }


            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController, mainViewModel: MainViewModel, productViewModel: ProductViewModel) {
    val productList = listOf(
        Product(1, "Teclado Gamer", "Optimo para juego.", 124.99, "https://cdn2.unrealengine.com/mechanical-keyboard-diagonal-4080x2295-d50ff434f19c.jpg"),
        Product(2, "Audifonos Gamer", "Esenciales para gaming .", 99.99, "https://media.spdigital.cl/thumbnails/products/53_teu5s_b63b51ea_thumbnail_512.png"),
        Product(3, "Silla Gamer", "Con respaldo para pasar horas en pantalla.", 150.00, "https://ulrikgaming.cl/wp-content/uploads/2021/06/60709d0491072d0dcd3fbed8.jpg"),
        Product(4, "Polera Faker", "OMG la polera de el jugador profesional de lol.", 500.00, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSU6z0c2VuKWtZVUvBziuOqebAVjzshuczLOg&s"),
        Product(5, "Catan", "Juego de mesa para jugar en familia.", 39.99, "https://devirinvestments.s3.eu-west-1.amazonaws.com/img/catalog/product/8436017220100-1200-face3d.jpg"),
        Product(6, "Mouse Gamer", "Mouse gamer con luces para mas FPS.", 49.99, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0PXR5i2iWEowmbeQ1LeNadu1TH7MPGoVDeg&s"),
        Product(7, "Monopoly", "Conocido Juego de mesa para jugar con amigos.", 35.00, "https://m.media-amazon.com/images/I/71Tks9Tf7aL._AC_SL1000_.jpg")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Catálogo de Productos",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {navController.navigate(AppRoute.Cart.route)}
                        ) {
                            Icon(imageVector =  Icons.Default.ShoppingCartCheckout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background)
                        }
                    }
                },
            )
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = { navController.navigate(AppRoute.Home.route) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(productList) { product ->
                ProductItem(product = product, productViewModel = productViewModel)
            }
        }
    }
}
