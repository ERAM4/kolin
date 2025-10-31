package com.example.teacherstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Scaffold (){innerPadding->
        Column (horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
            Text("Welcum to Level Up-gamer!")
            Button(onClick = {
                viewModel.navigateTo(AppRoute.Login)
            }
            ) {
                Text("Go to login")
            }
            Button(onClick = {
                viewModel.navigateTo(AppRoute.Register)
            }) {
                Text("Go to register")
            }
        }



    }
}