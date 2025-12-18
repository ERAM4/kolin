package com.example.teacherstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.model.database.AuthViewModel
import com.example.teacherstore.navigation.AppRoute

// 1. PANTALLA INTELIGENTE
@Composable
fun RegistroScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoading by authViewModel.isLoading.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val registroExitoso by authViewModel.registroExitoso.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            Toast.makeText(context, "¡Cuenta creada! Inicia sesión.", Toast.LENGTH_LONG).show()
            navController.navigate(AppRoute.Login.route) { popUpTo(AppRoute.Login.route) { inclusive = true } }
            authViewModel.limpiarEstados()
        }
    }

    LaunchedEffect(mensajeError) {
        if (mensajeError != null) Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show()
    }

    RegistroContent(
        isLoading = isLoading,
        onNavigateLogin = { navController.navigate(AppRoute.Login.route) },
        onRegistrar = { nombre, cp, correo, pass ->
            authViewModel.registrar(nombre, cp, correo, pass)
        }
    )
}

// 2. UI PURA
@Composable
fun RegistroContent(
    isLoading: Boolean,
    onNavigateLogin: () -> Unit,
    onRegistrar: (String, String, String, String) -> Unit
) {
    var nombreUsuario by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf(false) }
    var cpError by remember { mutableStateOf(false) }
    var correoError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var repetirPasswordError by remember { mutableStateOf(false) }
    var terminosError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues).padding(24.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("CREAR CUENTA", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))

            // Campos
            OutlinedTextField(
                value = nombreUsuario, onValueChange = { nombreUsuario = it; nombreError = false },
                label = { Text("Gamertag (Usuario)") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), isError = nombreError,
                supportingText = { if (nombreError) Text("Obligatorio", color = MaterialTheme.colorScheme.error) },
                trailingIcon = { if (nombreError) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = codigoPostal, onValueChange = { codigoPostal = it; cpError = false },
                label = { Text("Código Postal") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = cpError,
                supportingText = { if (cpError) Text("Obligatorio", color = MaterialTheme.colorScheme.error) },
                trailingIcon = { if (cpError) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = correo, onValueChange = { correo = it; correoError = false },
                label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), isError = correoError,
                supportingText = { if (correoError) Text("Correo inválido", color = MaterialTheme.colorScheme.error) },
                trailingIcon = { if (correoError) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it; passwordError = false },
                label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = passwordError,
                supportingText = { if (passwordError) Text("Obligatoria", color = MaterialTheme.colorScheme.error) },
                trailingIcon = { if (passwordError) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = repetirPassword, onValueChange = { repetirPassword = it; repetirPasswordError = false },
                label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = repetirPasswordError,
                supportingText = { if (repetirPasswordError) Text("No coinciden", color = MaterialTheme.colorScheme.error) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Checkbox CON MEJORA DE CLICK
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it; terminosError = false },
                    colors = CheckboxDefaults.colors(checkedColor = if (terminosError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(8.dp))

                // --- AQUÍ ESTÁ EL ARREGLO ---
                // Hacemos que el texto sea clickeable
                Text(
                    text = "Acepto los términos",
                    color = if (terminosError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        aceptaTerminos = !aceptaTerminos
                        terminosError = false
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Boton
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                Button(
                    onClick = {
                        nombreError = nombreUsuario.isBlank()
                        cpError = codigoPostal.isBlank()
                        correoError = correo.isBlank() || !correo.contains("@")
                        passwordError = password.isBlank()
                        repetirPasswordError = repetirPassword.isBlank() || password != repetirPassword
                        terminosError = !aceptaTerminos

                        if (!nombreError && !cpError && !correoError && !passwordError && !repetirPasswordError && !terminosError) {
                            onRegistrar(nombreUsuario, codigoPostal, correo, password)
                        } else {
                            Toast.makeText(context, "Corrige los errores", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("REGISTRARSE", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("¿Ya tienes cuenta? ", color = MaterialTheme.colorScheme.onBackground)
                Text("Inicia sesión", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateLogin() })
            }
        }
    }
}