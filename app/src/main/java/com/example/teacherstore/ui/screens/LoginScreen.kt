package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.model.database.AuthViewModel // Tu nuevo ViewModel
import com.example.teacherstore.navigation.AppRoute

@Composable
fun LoginScreen(
    navController: NavController, // <--- Este va primero para que coincida con MainActivity
    authViewModel: AuthViewModel = viewModel() // <--- Usamos el AuthViewModel conectado a Spring Boot
) {
    // ESTADOS LOCALES (Inputs del formulario)
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ESTADOS QUE VIENEN DEL SERVIDOR
    val isLoading by authViewModel.isLoading.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val token by authViewModel.loginExitoso.collectAsState()

    val context = LocalContext.current

    // --- 1. SI EL LOGIN ES EXITOSO (Llega el Token) ---
    LaunchedEffect(token) {
        if (token != null) {
            Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
            // Navegamos al Home
            navController.navigate(AppRoute.Home.route) {
                // Esto evita que al dar "Atrás" vuelvas al login
                popUpTo(AppRoute.Login.route) { inclusive = true }
            }
        }
    }

    // --- 2. SI HAY ERROR (Ej: contraseña mal) ---
    LaunchedEffect(mensajeError) {
        if (mensajeError != null) {
            Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show()
        }
    }

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

            // CAMPO CORREO
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CAMPO CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN LOGIN
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // AQUÍ LLAMAMOS A SPRING BOOT
                        if (correo.isNotBlank() && password.isNotBlank()) {
                            authViewModel.login(correo, password)
                        } else {
                            Toast.makeText(context, "Por favor llena los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Iniciar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // IR A REGISTRO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tienes una cuenta? ")
                Text(
                    text = "¡Crea una!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        // Corregido: Antes ibas al Home, ahora vas al Registro
                        navController.navigate(AppRoute.Registro.route)
                    }
                )
            }
        }
    }
}