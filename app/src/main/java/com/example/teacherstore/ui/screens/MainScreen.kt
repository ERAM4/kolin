package com.example.teacherstore.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // Ya no definimos colores aquí. El Scaffold tomará el "GamerBackground" del Theme.kt

    Scaffold { innerPadding -> 

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp), // Un poco más de margen a los lados
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // TÍTULO GRANDE
            Text(
                text = "LEVEL UP-GAMER",
                color = MaterialTheme.colorScheme.primary, // NeonCyan
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp // Espaciado entre letras estilo Arcade
            )

            // SUBTÍTULO BLANCO
            Text(
                text = "Tu tienda definitiva",
                color = MaterialTheme.colorScheme.onBackground, // Blanco
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(48.dp))

            // BOTÓN PRINCIPAL (SÓLIDO - CYAN)
            Button(
                onClick = { viewModel.navigateTo(AppRoute.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp), // Bordes redondeados modernos
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary // Negro
                )
            ) {
                Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN SECUNDARIO (OUTLINED - SOLO BORDE)
            // Esto le da jerarquía visual: El login es más importante, el registro es secundario
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppRoute.Registro) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary) // Borde Cyan
            ) {
                Text("REGISTRARSE", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DISCRETO PARA AYUDA
            TextButton(
                onClick = { viewModel.navigateTo(AppRoute.Help) }
            ) {
                Text(
                    "¿Necesitas ayuda o soporte?",
                    color = MaterialTheme.colorScheme.secondary // NeonPurple
                )
            }
        }
    }
}