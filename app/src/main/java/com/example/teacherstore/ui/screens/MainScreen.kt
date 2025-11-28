package com.example.teacherstore.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.ui.theme.Cyan80
import com.example.teacherstore.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController
){
    // Solid background color that matches the Cyan theme
    val backgroundColor = Color(0xFF001F1F) // Dark Cyan/Black mix for solid background

    Scaffold(
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            // Ocupa todo el espacio y centra el contenido
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido a Level Up-gamer!",
                color = Cyan80 // Cyan text to pop against dark background
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.navigateTo(AppRoute.Login)
            }) {
                Text("Iniciar sesion")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.navigateTo(AppRoute.Registro)
            }) {
                Text("Registrarse")
            }
            
             Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.navigateTo(AppRoute.Help)
            }) {
                Text("Ayuda y Soporte")
            }
        }
    }
}
