package com.example.ucbp1.features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ucbp1.navigation.Screen
@OptIn(ExperimentalMaterial3Api::class) // 👈 AGREGA ESTA LÍNEA

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MenuButton("GitHub") { navController.navigate(Screen.Github.route) }
            }
            item {
                MenuButton("Dólar") { navController.navigate(Screen.Dollar.route) }
            }
            item {
                MenuButton("Películas") { navController.navigate(Screen.PopularMovies.route) }
            }
            item {
                MenuButton("Perfil") { navController.navigate(Screen.Profile.route) }
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
