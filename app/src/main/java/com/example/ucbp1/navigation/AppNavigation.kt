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
import com.example.ucbp1.features.home.presentation.HomeScreen
import com.example.ucbp1.features.movie.presentation.PopularMoviesViewModel
import org.koin.androidx.compose.koinViewModel

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

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(Screen.Dollar.route) {
            DollarScreen()
        }

        composable(Screen.PopularMovies.route) {
            val popularMoviesViewModel: PopularMoviesViewModel = koinViewModel()
            PopularMoviesScreen(viewModel = popularMoviesViewModel)

        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}
