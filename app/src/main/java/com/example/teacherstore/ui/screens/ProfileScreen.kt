package com.example.teacherstore.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.teacherstore.navigation.AppRoute
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
    viewModel: MainViewModel= viewModel(),
    navController: NavController,
    usuarioViewModel: UsuarioViewModel = viewModel()
){
    val items= listOf(AppRoute.Home, AppRoute.Profile)
    var selectedItem by remember { mutableIntStateOf(1) }

    val estado by usuarioViewModel.estado.collectAsState()

    // Local state for editing mode
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Store both URI and File Path
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Function to create the image file
    fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
            )
        } catch (ex: IOException) {
            Toast.makeText(context, "Error creating file: ${ex.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

    // Temporary URI holder for the camera intent
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                currentPhotoUri = tempImageUri
                Toast.makeText(context, "Photo saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No photo taken.", Toast.LENGTH_SHORT).show()
            }
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
                Toast.makeText(context, "Camera permissions needed.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    fun checkAndRequestPermissions() {
        val permissionsToRequest = arrayOf(Manifest.permission.CAMERA)

        if (permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
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
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    Scaffold(
        bottomBar ={
            NavigationBar {
                items.forEachIndexed { index,approute ->
                    NavigationBarItem(
                        selected= selectedItem==index,
                        onClick = {
                            selectedItem=index
                            viewModel.navigateTo(approute)
                        },
                        label = {Text(approute.route)},
                        icon = {
                            Icon(imageVector = if(approute== AppRoute.Home) Icons.Default.Home else Icons.Default.Person,
                                contentDescription = approute.route
                            )
                        }
                    )
                }
            }
        }
    )
    { innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // Display Image
            Image(
                painter = rememberAsyncImagePainter(
                    model = currentPhotoUri
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                checkAndRequestPermissions()
            }) {
                Text(text = "Cambiar Foto")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Info Section
            if (isEditing) {
                OutlinedTextField(
                    // CORREGIDO: estado.username en lugar de estado.nombre
                    value = estado.username,
                    onValueChange = { usuarioViewModel.onNombreChange(it) },
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = { usuarioViewModel.onCorreoChange(it) },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    isEditing = false
                    // Aquí llamarías a update user...
                }) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Guardar")
                }
            } else {
                // Display Mode
                Text(
                    // CORREGIDO: estado.username
                    text = "Usuario: ${estado.username.ifBlank { "Sin nombre" }}",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Correo: ${estado.correo.ifBlank { "correo@ejemplo.com" }}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Editar Perfil")
                }
            }
        }
    }
}