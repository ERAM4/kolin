package com.example.teacherstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teacherstore.model.database.ProductDataBase
import com.example.teacherstore.model.database.UserDataBase
import com.example.teacherstore.model.database.repository.ProductRepository
import com.example.teacherstore.model.database.repository.UserRepository
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.navigation.NavigationEvent
import com.example.teacherstore.ui.screens.CartScreen
import com.example.teacherstore.ui.screens.CatalogScreen
import com.example.teacherstore.ui.screens.HelpScreen
import com.example.teacherstore.ui.screens.HomeScreen
import com.example.teacherstore.ui.screens.LoginScreen
import com.example.teacherstore.ui.screens.MainScreen
import com.example.teacherstore.ui.screens.ProfileScreen
import com.example.teacherstore.ui.screens.RegistroScreen
import com.example.teacherstore.ui.theme.TeacherStoreTheme
import com.example.teacherstore.viewmodel.MainViewModel
import com.example.teacherstore.viewmodel.ProductViewModel
import com.example.teacherstore.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TeacherStoreTheme {
                val mainViewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                // Configuración de Room (Base de datos local)
                val userDataBase = UserDataBase.getDataBase(this)
                val userRepository = UserRepository(userDataBase.userDao())
                val userFactory = UsuarioViewModel.UsuarioViewModelFactory(userRepository)
                val usuarioViewModel: UsuarioViewModel = viewModel(factory = userFactory)

                val productDataBase = ProductDataBase.getDataBase(this)
                val productRepository = ProductRepository(productDataBase.productDao())
                val productFactory = ProductViewModel.DataProductViewModelFactory(productRepository)
                val productViewModel: ProductViewModel = viewModel(factory = productFactory)

                // Efecto para manejar eventos de navegación globales
                LaunchedEffect(Unit) {
                    mainViewModel.navEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.appRoute.route) {
                                    event.popUpRoute?.let {
                                        popUpTo(it.route) {
                                            inclusive = event.inclusive
                                        }
                                        launchSingleTop = event.singleTop
                                        restoreState = true
                                    }
                                }
                            }
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRoute.Main.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // --- PANTALLASs ---

                        composable(AppRoute.Home.route) {
                            HomeScreen(mainViewModel, navController)
                        }

                        composable(AppRoute.Registro.route) {
                            RegistroScreen(navController = navController)
                        }

                        composable(AppRoute.Profile.route) {
                            ProfileScreen(mainViewModel, navController, usuarioViewModel)
                        }

                        composable(AppRoute.Settings.route) {
                            // SettingScreen(navController, viewModel)
                        }

                        composable(AppRoute.Help.route) {
                            HelpScreen(navController)
                        }

                        composable(AppRoute.Main.route) {
                            MainScreen(mainViewModel, navController)
                        }

                        // --- AQUÍ ESTABA EL ERROR: FALTABAN ESTAS PANTALLAS ---

                        // 1. LOGIN (Necesario para que no crashee tras registrarse)
                        composable(AppRoute.Login.route) {
                            LoginScreen(navController = navController)
                        }

                        // 2. CATÁLOGO
                        composable(AppRoute.Catalog.route) {
                            CatalogScreen(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                productViewModel = productViewModel
                            )
                        }

                        // 3. CARRITO
                        composable(AppRoute.Cart.route) {
                            CartScreen(
                                navController = navController,
                                productViewModel = productViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}