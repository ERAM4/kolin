package com.example.teacherstore.navigation

sealed class AppRoute(val route:String) {
    data object Home:AppRoute("home")
    data object Register: AppRoute("register")
    data object Profile: AppRoute("profile")
    data object Settings: AppRoute("settings")

    data object Main: AppRoute("main")
    data object Login: AppRoute("login")

    data object Shop: AppRoute("shop")

    data object Catalog: AppRoute("catalog")
    data object Cart: AppRoute("Cart")

    data class Detail (val itemId:String): AppRoute("detail/{itemId}")
    {
        fun buildRoute():String{
            return route.replace("{itemId}",itemId)
        }
    }


}