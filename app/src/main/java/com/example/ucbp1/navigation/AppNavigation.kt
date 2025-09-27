package com.example.ucbp1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ucbp1.features.github.presentation.GithubScreen
import com.example.ucbp1.features.profile.application.ProfileScreen
import com.example.ucbp1.features.dollar.presentation.DollarScreen
import com.example.ucbp1.features.movie.presentation.PopularMoviesScreen
import com.example.ucbp1.features.login.presentation.LoginScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Github.route) {
            GithubScreen(modifier = Modifier)
        }
        composable(Screen.Home.route) {

        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(Screen.Dollar.route) {
            DollarScreen()
        }

        composable(Screen.PopularMovies.route) {
            PopularMoviesScreen()
        }

        composable(Screen.Login.route) {
            LoginScreen()
        }
    }
}