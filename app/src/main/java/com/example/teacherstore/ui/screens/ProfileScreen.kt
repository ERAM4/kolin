package com.example.teacherstore.ui.screens

// Imports de tu código original
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.navigation.AppRoute

// Imports añadidos para la cámara
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter // <--- ¡IMPORTANTE!
import com.example.teacherstore.viewmodel.MainViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel= viewModel(),
    navController: NavController
){
    // Tu lógica original del BottomBar
    val items= listOf(AppRoute.Home, AppRoute.Profile)
    var selectedItem by remember { mutableIntStateOf(1) } // Sigue en 1 para que "Profile" esté seleccionado

    // --- INICIO: Lógica de la Cámara (Parte 5) ---
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

    // Función para crear el archivo de imagen
    fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                imageFileName,  /* prefijo */
                ".jpg",         /* sufijo */
                storageDir      /* directorio */
            )
            // Guardamos la ruta absoluta del archivo:
            currentPhotoPath = image.absolutePath
            image
        } catch (ex: IOException) {
            Toast.makeText(context, "Error al crear el archivo: ${ex.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

    // Launcher para la CÁMARA
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // ¡ESTA ES LA LÍNEA CLAVE!
                // Forzamos la actualización del estado para que Compose
                // y Coil vuelvan a cargar la imagen desde el archivo, que ahora sí existe.
                val path = currentPhotoPath
                currentPhotoPath = null // Lo ponemos a null
                currentPhotoPath = path // Lo volvemos a poner a su valor

                Toast.makeText(context, "Foto guardada y mostrada.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No se tomó ninguna foto.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher para PERMISOS
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.values.all { it }) {
                // Permisos Concedidos: Creamos el archivo y lanzamos la cámara
                val photoFile: File? = createImageFile()
                photoFile?.let {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.applicationContext.packageName}.fileprovider",
                        it
                    )
                    imageUri = photoURI // Guardamos la URI (aunque la imagen se mostrará con el 'path')
                    takePictureLauncher.launch(photoURI) // ¡Lanzamos la cámara!
                }
            } else {
                Toast.makeText(context, "Se necesitan permisos de cámara.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Función para verificar y pedir permisos
    fun checkAndRequestPermissions() {
        val permissionsToRequest = arrayOf(Manifest.permission.CAMERA)

        if (permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            // Si ya los tenemos, lanzamos la cámara
            val photoFile: File? = createImageFile()
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.applicationContext.packageName}.fileprovider",
                    it
                )
                imageUri = photoURI
                takePictureLauncher.launch(photoURI)
            }
        } else {
            // Si no, pedimos los permisos
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }
    // --- FIN: Lógica de la Cámara ---


    // --- INICIO: UI (Scaffold) ---
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
        // Este es el contenido principal de tu pantalla
        Column(
            modifier = Modifier
                .padding(innerPadding) // <-- ¡Muy importante! Aplica el padding del Scaffold
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // --- INICIO: UI de la Cámara (Parte 4) ---

            // El ImageView que mostrará la foto
            Image(
                painter = rememberAsyncImagePainter(
                    // Usamos la RUTA (currentPhotoPath) para que Coil la recargue
                    model = currentPhotoPath ?: Color.Gray
                ),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape) // La hacemos redonda
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            // El Botón que inicia el proceso
            Button(onClick = {
                checkAndRequestPermissions() // Al hacer clic, verificamos permisos
            }) {
                Text(text = "Tomar Foto")
            }

            // --- FIN: UI de la Cámara ---
        }
    }
    // --- FIN: UI (Scaffold) ---
}