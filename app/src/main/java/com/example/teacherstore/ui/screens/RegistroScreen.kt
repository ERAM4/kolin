package com.example.teacherstore.ui.screens

import android.widget.Toast
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
import com.example.teacherstore.model.database.AuthViewModel // Tu ViewModel nuevo
import com.example.teacherstore.navigation.AppRoute

@Composable
fun RegistroScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Usamos el AuthViewModel conectado a Retrofit
) {
    // ESTADOS DEL FORMULARIO (Solo lo que pide el Backend)
    var nombreUsuario by remember { mutableStateOf("") } // Se enviará como 'username'
    var correo by remember { mutableStateOf("") }       // Se enviará como 'correo'
    var password by remember { mutableStateOf("") }     // Se enviará como 'password'
    var repetirPassword by remember { mutableStateOf("") } // Solo validación visual

    var aceptaTerminos by remember { mutableStateOf(false) }

    // ESTADOS QUE VIENEN DEL VIEWMODEL (RESPUESTA DEL SERVIDOR)
    val isLoading by authViewModel.isLoading.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()

    val context = LocalContext.current

    // EFECTO: Si el registro es exitoso, volvemos al Login
    LaunchedEffect(mensajeError) {
        if (mensajeError?.contains("exitoso", ignoreCase = true) == true) {
            Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
            navController.navigate(AppRoute.Login.route)
        }
    }

    Scaffold(
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(text = "Crear Cuenta", style = MaterialTheme.typography.headlineMedium)

                // Mensaje de error del servidor (ej: "Error 500" o "Usuario ya existe")
                if (mensajeError != null && !mensajeError!!.contains("exitoso", ignoreCase = true)) {
                    Text(text = mensajeError!!, color = MaterialTheme.colorScheme.error)
                }

                // 1. INPUT USERNAME (Coincide con User.java: private String username)
                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = { nombreUsuario = it },
                    label = { Text("Nombre de Usuario") }, // Antes "Nombre"
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. INPUT CORREO (Coincide con User.java: private String correo)
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. INPUT PASSWORD (Coincide con User.java: private String password)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // 4. INPUT REPETIR PASSWORD (Solo validación visual en Android)
                OutlinedTextField(
                    value = repetirPassword,
                    onValueChange = { repetirPassword = it },
                    label = { Text("Repetir Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = (repetirPassword.isNotEmpty() && password != repetirPassword),
                    supportingText = {
                        if (repetirPassword.isNotEmpty() && password != repetirPassword) {
                            Text("Las contraseñas no coinciden")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // --- ¡IMPORTANTE! ---
                // Se eliminaron "Dirección" y "País" porque tu modelo User.java NO los tiene.
                // --------------------

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = aceptaTerminos,
                        onCheckedChange = { aceptaTerminos = it }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Acepto los términos y condiciones")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN DE REGISTRO
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            // Validaciones simples antes de molestar al servidor
                            if (nombreUsuario.isBlank() || correo.isBlank() || password.isBlank()) {
                                Toast.makeText(context, "Faltan datos", Toast.LENGTH_SHORT).show()
                            } else if (password != repetirPassword) {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                            } else if (!aceptaTerminos) {
                                Toast.makeText(context, "Acepta los términos", Toast.LENGTH_SHORT).show()
                            } else {
                                // LLAMADA FINAL: Enviamos solo lo que User.java espera
                                authViewModel.registrar(nombreUsuario, correo, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Registrarse")
                    }
                }

                TextButton(onClick = { navController.navigate(AppRoute.Login.route) }) {
                    Text(text = "¿Ya tienes cuenta? Iniciar sesión")
                }
            }
        }
    )
}