package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

// 1. ITEM VISUAL (Sin ViewModel, recibe una acción 'onAddToCart')
@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAddToCart: () -> Unit // Cambio clave: Pasamos la acción como parámetro
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Image),
                error = rememberVectorPainter(Icons.Default.BrokenImage),
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(Color.Black)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${"%.2f".format(product.price)}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.ExtraBold)

                    IconButton(
                        onClick = onAddToCart, // Usamos la lambda
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = "Agregar", tint = Color.Black)
                    }
                }
            }
        }
    }
}


@Composable
fun CatalogScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current

    // Lista de productos
    val productList = listOf(
        Product(1, "Teclado Mecánico RGB", "Iluminación Chroma, switches azules clicky.", 124.99, "https://images.unsplash.com/photo-1595225476474-87563907a212?auto=format&fit=crop&w=500&q=60"),
        Product(2, "Headset Pro Gamer", "Sonido envolvente 7.1 y luces LED.", 99.99, "https://d253gaikpmhopn.cloudfront.net/wp-content/uploads/2021/04/EPOS-Sennheiser-GSP300-Gaming-Headset-786x1024.jpg"),
        Product(3, "Silla Gamer Elite", "Diseño ergonómico para largas sesiones.", 250.00, "https://images.unsplash.com/photo-1598550476439-6847785fcea6?auto=format&fit=crop&w=500&q=60"),
        Product(4, "Setup Completo", "Escritorio gamer con gestión de cables.", 450.00, "https://th.bing.com/th/id/R.0d2effe49b1471e5d22b42ba33a948bb?rik=f6Tc9HfV3%2fbKjg&pid=ImgRaw&r=0"),
        Product(5, "Mouse Ultralight", "Sensor óptico de alta precisión, RGB.", 49.99, "https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=500&q=60"),
        Product(6, "Monitor Curvo 144Hz", "Inmersión total con panel 4K.", 299.99, "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=500&q=60"),
        Product(7, "Consola Portátil", "Juega donde quieras con máxima potencia.", 350.00, "https://images.unsplash.com/photo-1486401899868-0e435ed85128?auto=format&fit=crop&w=500&q=60"),
        Product(8, "Control Pro", "Mando inalámbrico con agarre texturizado.", 69.99, "https://cdn.gameplanet.com/wp-content/uploads/2022/09/02043111/control-rojo-negro.jpg"),
        Product(9, "Micrófono Streamer", "Calidad de estudio con filtro pop incluido.", 129.99, "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?auto=format&fit=crop&w=500&q=60"),
        Product(10, "Gafas VR Future", "Realidad virtual inmersiva 8K.", 399.00, "https://cdn.computerhoy.com/sites/navi.axelspringer.es/public/media/image/2022/11/fiyapoo-2864545.jpg?tf=1200x")
    )

    // Llamamos a la versión Visual
    CatalogContent(
        products = productList,
        onBackClick = { navController.navigate(AppRoute.Home.route) },
        onCartClick = { navController.navigate(AppRoute.Cart.route) },
        onAddToCart = { product ->
            productViewModel.insertProduct(
                DataProduct(product.id, product.name, product.price, product.description,product.imageUrl)
            )
            Toast.makeText(context, "Añadido al Loot!", Toast.LENGTH_SHORT).show()
        }
    )
}

// 3. PANTALLA VISUAL (Esta es la que vamos a TESTEAR)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogContent(
    products: List<Product>,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CATÁLOGO", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, letterSpacing = 2.sp) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.primary) } },
                actions = { IconButton(onClick = onCartClick) { Icon(Icons.Default.ShoppingCart, "Carrito", tint = MaterialTheme.colorScheme.secondary) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onAddToCart = { onAddToCart(product) }
                )
            }
        }
    }
}