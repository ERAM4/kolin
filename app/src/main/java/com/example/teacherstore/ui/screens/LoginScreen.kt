package com.example.teacherstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teacherstore.navigation.AppRoute
import com.example.teacherstore.viewmodel.MainViewModel
import com.example.teacherstore.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    navController: NavController
){

    val estado by usuarioViewModel.loginState.collectAsState()
    val coroutine = rememberCoroutineScope()

    Scaffold (){innerPadding->
        Column (horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp)){

            OutlinedTextField(
                value = estado.name,
                onValueChange = usuarioViewModel::onNameChangeLogin,
                label = {Text("Correo electronico")},
                //isError = estado.errores.nombre!=null,
                singleLine = true,
                supportingText = {

                },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = estado.password,
                onValueChange = usuarioViewModel::onPasswordChangeLogin,
                label = {Text("Contraseña")},
               // isError = estado.errores.nombre!=null,
                singleLine = true,
                supportingText = {
                },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(onClick = {
                navController.navigate(AppRoute.Home.route)
            }) {
                Text("¿No tienes una cuenta? ¡Crea una!")
            }
            Button(onClick = {
                coroutine.launch {
                    if (usuarioViewModel.userExist(email = estado.name, password = estado.password)
                    ) {
                        navController.navigate(AppRoute.Home.route)
                    }
                }
            }) {
                Text("Iniciar sesion")
            }
        }



    }
}