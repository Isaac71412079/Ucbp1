package com.example.ucbp1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun AppNavigation(
    navigationViewModel: NavigationViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        when (command.options) {
                            NavigationViewModel.NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0) // Limpiar todo el back stack
                            }
                            NavigationViewModel.NavigationOptions.REPLACE_HOME -> {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            }
                            else -> {
                            }
                        }
                    }
                }
                is NavigationViewModel.NavigationCommand.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier // Aplica el modifier aquí
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
        /*
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        */
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}
