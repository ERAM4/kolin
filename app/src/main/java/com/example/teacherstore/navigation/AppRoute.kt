package com.example.teacherstore.navigation

sealed class AppRoute(val route:String) {
    data object Home:AppRoute("home")

    // CAMBIO AQUÍ: De 'Register' a 'Registro' para que coincida con LoginScreen y tu archivo .kt
    data object Registro: AppRoute("register")
    data object Profile: AppRoute("profile")
    data object Settings: AppRoute("settings")
    data object Help: AppRoute("help")
    data object Main: AppRoute("main")
    data object Login: AppRoute("login")
    data object Shop: AppRoute("shop")
    data object Catalog: AppRoute("catalog")
    data object Cart: AppRoute("Cart")

    data class Detail (val itemId:String): AppRoute("detail/{itemId}") {
        fun buildRoute():String{
            return route.replace("{itemId}",itemId)
        }
    }
}