package com.example.teacherstore.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.utils.SessionManager// Asegúrate de importar tu SessionManager
import com.example.teacherstore.viewmodel.MainViewModel
import com.example.teacherstore.viewmodel.UsuarioViewModel

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController,
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Obtenemos los datos del ViewModel
    val estado by usuarioViewModel.estado.collectAsState()

    // Estado local para edición
    var isEditing by remember { mutableStateOf(false) }

    // --- LÓGICA DE LA CÁMARA (Sin cambios, solo funciona) ---
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (ex: IOException) {
            null
        }
    }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) currentPhotoUri = tempImageUri
        }
    )
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.values.all { it }) {
                val photoFile: File? = createImageFile()
                photoFile?.let {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.applicationContext.packageName}.fileprovider",
                        it
                    )
                    tempImageUri = photoURI
                    takePictureLauncher.launch(photoURI)
                }
            } else {
                Toast.makeText(context, "Se necesitan permisos de cámara", Toast.LENGTH_SHORT).show()
            }
        }
    )

    fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val photoFile: File? = createImageFile()
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.applicationContext.packageName}.fileprovider",
                    it
                )
                tempImageUri = photoURI
                takePictureLauncher.launch(photoURI)
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Fondo Oscuro

        // BARRA INFERIOR (Bottom Bar)
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface, // Gris oscuro
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                // HOME
                NavigationBarItem(
                    selected = false,
                    onClick = { viewModel.navigateTo(AppRoute.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Lobby") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                // PERFIL (Seleccionado)
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estamos aquí */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary, // Cyan
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.surface // Quita el óvalo de fondo
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TÍTULO
            Text(
                text = "PERFIL DE JUGADOR",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- FOTO DE PERFIL ---
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = rememberAsyncImagePainter(model = currentPhotoUri),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape) // Borde Cyan
                        .background(MaterialTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop
                )

                // Botón pequeño de cámara
                IconButton(
                    onClick = { checkAndRequestPermissions() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary) // Fondo Morado
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- TARJETA DE INFORMACIÓN ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    if (isEditing) {
                        // MODO EDICIÓN
                        OutlinedTextField(
                            value = estado.username,
                            onValueChange = { usuarioViewModel.onNombreChange(it) },
                            label = { Text("Gamertag") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Person, null) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = estado.correo,
                            onValueChange = { usuarioViewModel.onCorreoChange(it) },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Email, null) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { isEditing = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Save, null, tint = Color.Black)
                            Spacer(Modifier.width(8.dp))
                            Text("GUARDAR CAMBIOS", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                    } else {
                        // MODO LECTURA (Stats)
                        ProfileStatItem(label = "GAMERTAG", value = estado.username.ifBlank { "Player 1" }, icon = Icons.Default.Person)
                        Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
                        ProfileStatItem(label = "CORREO", value = estado.correo.ifBlank { "test@gamer.com" }, icon = Icons.Default.Email)

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedButton(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, null)
                            Spacer(Modifier.width(8.dp))
                            Text("EDITAR PERFIL")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja lo siguiente al fondo

            // --- BOTÓN CERRAR SESIÓN ---
            Button(
                onClick = {
                    // 1. Borrar sesión
                    sessionManager.clearData()
                    // 2. Ir al Login y borrar historial
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0) { inclusive = true } // Borra toda la pila de navegación
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("CERRAR SESIÓN")
            }
        }
    }
}

// Componente auxiliar para mostrar datos bonitos
@Composable
fun ProfileStatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        }
    }
}