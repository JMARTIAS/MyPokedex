package com.example.mypokedex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mypokedex.presentation.auth.AuthScreen
import com.example.mypokedex.presentation.detail.DetailScreen
import com.example.mypokedex.presentation.favorites.FavoritesScreen
import com.example.mypokedex.presentation.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(route = Screen.Auth.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onPokemonClick = {
                    navController.navigate(Screen.Detail.createRoute(it))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }
        composable(route = Screen.Detail.route) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onPokemonClick = {
                     navController.navigate(Screen.Detail.createRoute(it))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
