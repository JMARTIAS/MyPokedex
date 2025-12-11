package com.example.mypokedex.presentation.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Detail : Screen("detail/{pokemonName}") {
        fun createRoute(pokemonName: String) = "detail/$pokemonName"
    }
    object Favorites : Screen("favorites")
}
