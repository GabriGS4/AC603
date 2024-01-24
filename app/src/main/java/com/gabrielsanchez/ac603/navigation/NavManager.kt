package com.gabrielsanchez.ac603.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabrielsanchez.ac603.models.Articulos
import com.gabrielsanchez.ac603.viewmodels.ArticulosViewModel
import com.gabrielsanchez.ac603.views.ArticulosHomeView
import com.gabrielsanchez.ac603.views.EditArticuloView
import com.gabrielsanchez.ac603.views.NewArticuloView

@Composable
fun NavManager(viewModel: ArticulosViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.Articulos.route) {
        composable(AppScreens.Articulos.route) {
            ArticulosHomeView(navController, viewModel)
        }

        composable(AppScreens.NewArticulo.route) {
            NewArticuloView(navController, viewModel)
        }

        composable(AppScreens.EditArticulo.route + "/{codigo}", arguments = listOf(
            navArgument("codigo") {
                type = NavType.IntType
            }
        )) {
            EditArticuloView(
                navController,
                viewModel,
                it.arguments!!.getInt("codigo"),
            )
        }
    }
}