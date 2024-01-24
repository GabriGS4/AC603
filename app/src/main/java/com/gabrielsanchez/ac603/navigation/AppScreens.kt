package com.gabrielsanchez.ac603.navigation

sealed class AppScreens(val route: String) {
    object Articulos : AppScreens("home")
    object NewArticulo : AppScreens("newArticulo")
    object EditArticulo : AppScreens("editArticulo")
}