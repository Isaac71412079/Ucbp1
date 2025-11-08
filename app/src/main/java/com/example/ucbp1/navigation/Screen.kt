package com.example.ucbp1.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Github: Screen("github")
    object Profile: Screen("profile")
    object CardExamples: Screen("card")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object Login: Screen("login")
    object MovieDetail : Screen("movie_detail")
    object Atulado: Screen("atulado")
}