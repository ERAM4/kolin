package com.example.teacherstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize // Importa fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding // Importa padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // Importa Modifier
import androidx.compose.ui.unit.dp // Importa dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController
){
    Scaffold { innerPadding ->
        Column(
            // Ocupa todo el espacio y centra el contenido
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido a Level Up-gamer!")

            Spacer(modifier = Modifier.height(16.dp)) // Añade espacio

            Button(onClick = {
                viewModel.navigateTo(AppRoute.Login)
            }) {
                Text("Iniciar sesion")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Añade espacio

            Button(onClick = {
                viewModel.navigateTo(AppRoute.Register)
            }) {
                Text("Registrarse")
            }
        }
    }
}
