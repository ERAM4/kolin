package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Importante para inyectar el ViewModel
import androidx.navigation.NavController
import com.example.teacherstore.model.database.AuthViewModel
import com.example.teacherstore.navigation.AppRoute

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Esto crea la instancia del ViewModel automáticamente
) {
    // ESTADOS LOCALES (Lo que el usuario escribe)
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ESTADOS DEL SERVIDOR (Observamos al ViewModel)
    val isLoading by authViewModel.isLoading.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val token by authViewModel.loginExitoso.collectAsState()

    val context = LocalContext.current

    // 1. REACCIÓN AL ÉXITO (Navegar al Home)
    LaunchedEffect(token) {
        if (token != null) {
            Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
            // Log de prueba para que veas el token en consola
            println("TOKEN RECIBIDO: $token")

            navController.navigate(AppRoute.Home.route) {
                popUpTo(AppRoute.Login.route) { inclusive = true }
            }
        }
    }

    // 2. REACCIÓN AL ERROR (Mostrar Toast)
    LaunchedEffect(mensajeError) {
        if (mensajeError != null) {
            Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
        }
    }

    // --- INTERFAZ DE USUARIO (UI) ---
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // AQUÍ  DISPARA LA CONEXIÓN
                        if (correo.isNotBlank() && password.isNotBlank()) {
                            authViewModel.login(correo, password)
                        } else {
                            Toast.makeText(context, "Por favor llena los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Iniciar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tienes una cuenta? ")
                Text(
                    text = "¡Crea una!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoute.Registro.route)
                    }
                )
            }
        }
    }
}