package com.example.ucbp1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

// Esta clase sellada representara los elementos de nuestro menu.
sealed class NavigationDrawer(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    // Usamos 'data object' para Singletons, es la práctica moderna
    data object Profile : NavigationDrawer(
        "Profile",
        Icons.Filled.Person, // Icono más apropiado
        Icons.Outlined.Person,
        Screen.Profile.route
    )

    data object Dollar : NavigationDrawer(
        "Dollar",
        Icons.Filled.ShoppingCart,
        Icons.Outlined.ShoppingCart,
        Screen.Dollar.route
    )

    data object Github : NavigationDrawer(
        "Github",
        Icons.Filled.Build, // Icono más apropiado
        Icons.Outlined.Build,
        Screen.Github.route
    )

    data object Movie : NavigationDrawer(
        "Movie",
        Icons.Filled.PlayArrow, // Icono más apropiado
        Icons.Outlined.PlayArrow,
        Screen.PopularMovies.route
    )
}
