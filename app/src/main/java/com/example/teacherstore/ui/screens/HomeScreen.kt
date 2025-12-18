package com.example.teacherstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.utils.SessionManager
import com.example.teacherstore.viewmodel.MainViewModel
import kotlinx.coroutines.launch

// Modelos de datos visuales
data class GameNews(val title: String, val subtitle: String, val imageUrl: String)
data class TrendingProduct(val name: String, val price: Double, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController
){
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // --- DATOS ---
    val featuredNews = listOf(
        GameNews("ELDEN RING: DLC", "La espera ha terminado", "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&w=800&q=80"),
        GameNews("POTENCIA RTX 4090", "Gráficos ultra realistas", "https://images.unsplash.com/photo-1591488320449-011701bb6704?auto=format&fit=crop&w=800&q=80"),
        GameNews("PLAYSTATION 5", "Stock disponible ahora", "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?auto=format&fit=crop&w=800&q=80")
    )

    val trendingProducts = listOf(
        TrendingProduct("Teclado RGB", 120.00, "https://images.unsplash.com/photo-1595225476474-87563907a212?auto=format&fit=crop&w=500&q=60"),
        TrendingProduct("Mouse Pro", 59.99, "https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=500&q=60"),
        TrendingProduct("Headset 7.1", 89.50, "https://images.unsplash.com/photo-1612731486859-963d76537704?auto=format&fit=crop&w=500&q=60")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("MENÚ PLAYER", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // OPCIONES MENÚ
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") }, icon = { Icon(Icons.Default.Person, null) }, selected = false,
                    onClick = { scope.launch { drawerState.close() }; viewModel.navigateTo(AppRoute.Profile) },
                    modifier = Modifier.padding(horizontal = 12.dp), colors = NavigationDrawerItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.primary)
                )
                NavigationDrawerItem(
                    label = { Text("Catálogo Completo") }, icon = { Icon(Icons.Default.List, null) }, selected = false,
                    onClick = { scope.launch { drawerState.close() }; viewModel.navigateTo(AppRoute.Catalog) },
                    modifier = Modifier.padding(horizontal = 12.dp), colors = NavigationDrawerItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.primary)
                )
                NavigationDrawerItem(
                    label = { Text("Soporte") }, icon = { Icon(Icons.Default.Help, null) }, selected = false,
                    onClick = { scope.launch { drawerState.close() }; viewModel.navigateTo(AppRoute.Help) },
                    modifier = Modifier.padding(horizontal = 12.dp), colors = NavigationDrawerItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.weight(1f))

                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") }, icon = { Icon(Icons.Default.Logout, null) }, selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        sessionManager.clearData()
                        navController.navigate(AppRoute.Login.route) { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier.padding(12.dp),
                    colors = NavigationDrawerItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.error, unselectedTextColor = MaterialTheme.colorScheme.error)
                )
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("LOBBY", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.navigateTo(AppRoute.Cart) }) {
                            Icon(Icons.Default.ShoppingCart, null, tint = MaterialTheme.colorScheme.secondary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // 1. BARRA DE BÚSQUEDA (NUEVO)
                SearchBarFake(onClick = { viewModel.navigateTo(AppRoute.Catalog) })

                Spacer(modifier = Modifier.height(16.dp))

                // 2. CARRUSEL DE NOTICIAS
                Text("NOVEDADES", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredNews) { news -> FeaturedCard(news) }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 3. OFERTA RELÁMPAGO (NUEVO - FLASH SALE)
                FlashSaleCard()

                Spacer(modifier = Modifier.height(32.dp))

                // 4. TENDENCIAS
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("MÁS VENDIDOS", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { viewModel.navigateTo(AppRoute.Catalog) }) {
                        Text("Ver todo", color = MaterialTheme.colorScheme.primary)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(trendingProducts) { product -> TrendingProductCard(product) }
                }
            }
        }
    }
}

// --- COMPONENTES VISUALES --

@Composable
fun SearchBarFake(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(25.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Buscar juegos, equipo...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun FlashSaleCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp)) // Icono Rayo
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("OFERTA FLASH", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Pack Streamer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text("-40% DESCUENTO", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://images.unsplash.com/photo-1593305841991-05c297f234ee?auto=format&fit=crop&w=400&q=60")
                    .crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

@Composable
fun FeaturedCard(news: GameNews) {
    Card(
        modifier = Modifier.width(280.dp).height(160.dp).clickable { },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(news.imageUrl).crossfade(true).build(),
                contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)), startY = 100f)))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(news.subtitle.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(news.title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TrendingProductCard(product: TrendingProduct) {
    Card(
        modifier = Modifier.width(140.dp).clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl).crossfade(true).build(),
                contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(100.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$${product.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
            }
        }
    }
}