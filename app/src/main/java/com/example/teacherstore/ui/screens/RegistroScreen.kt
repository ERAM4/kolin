package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


import com.example.teacherstore.model.database.AuthViewModel
import com.example.teacherstore.navigation.AppRoute

@Composable
fun RegistroScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    // ESTADOS DEL FORMULARIO
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // ESTADOS DEL VIEWMODEL
    val isLoading by authViewModel.isLoading.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val context = LocalContext.current

    // EFECTO: Si el registro es exitoso
    LaunchedEffect(mensajeError) {
        if (mensajeError?.contains("exitoso", ignoreCase = true) == true) {
            Toast.makeText(context, "Cuenta creada con éxito. ¡A jugar!", Toast.LENGTH_SHORT).show()
            navController.navigate(AppRoute.Login.route)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                    .verticalScroll(scrollState),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "CREAR CUENTA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de error
            if (mensajeError != null && !mensajeError!!.contains("exitoso", ignoreCase = true)) {
                Text(
                    text = mensajeError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // 1. USERNAME
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Gamertag (Usuario)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. CORREO
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. REPETIR PASSWORD
            OutlinedTextField(
                value = repetirPassword,
                onValueChange = { repetirPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = (repetirPassword.isNotEmpty() && password != repetirPassword),
                supportingText = {
                    if (repetirPassword.isNotEmpty() && password != repetirPassword) {
                        Text("Las contraseñas no coinciden")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CHECKBOX
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Acepto los términos y condiciones",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN REGISTRO
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                Button(
                    onClick = {
                        if (nombreUsuario.isBlank() || correo.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Faltan datos", Toast.LENGTH_SHORT).show()
                        } else if (password != repetirPassword) {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        } else if (!aceptaTerminos) {
                            Toast.makeText(context, "Debes aceptar los términos", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.registrar(nombreUsuario, correo, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("REGISTRARSE", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LINK AL LOGIN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Inicia sesión",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoute.Login.route)
                    }
                )
            }
        }
    }
}