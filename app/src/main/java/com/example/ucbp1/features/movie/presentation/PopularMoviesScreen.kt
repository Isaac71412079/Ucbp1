package com.example.ucbp1.features.movie.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PopularMoviesScreen(
    viewModel: PopularMoviesViewModel // Inyectado por Koin, o usa koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Para mostrar errores en el Snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short
                )
            }
            // Considera limpiar el error en el ViewModel después de mostrarlo
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Películas Populares") },
                actions = {
                    if (uiState.isUserInitiatedRefresh) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(onClick = { viewModel.refreshMoviesFromServer() }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refrescar películas"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading || (uiState.movies.isEmpty() && uiState.isUserInitiatedRefresh) -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.movies.isEmpty() && !uiState.isLoading && !uiState.isUserInitiatedRefresh -> {

                    if (uiState.error == null) {
                        Text(
                            "No se encontraron películas.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    // Si hay un error, el Box estará vacío y el Snackbar se mostrará.
                }

                uiState.movies.isNotEmpty() -> {
                    PopularMoviesView(
                        movies = uiState.movies,
                        onLikeClicked = { movieId ->
                            viewModel.onLikeClicked(movieId)
                        },
                        onMovieCardClicked = { movie ->
                            // TODO: Implementar navegación a detalles de la película
                            // Ejemplo: navController.navigate("movieDetail/${movie.id}")
                            println("Película clickeada: ${movie.title} (ID: ${movie.id})")
                        }
                    )
                }
            }
        }
    }
}

