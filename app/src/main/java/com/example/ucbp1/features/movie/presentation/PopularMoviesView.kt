package com.example.ucbp1.features.movie.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // Importar items para LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
//import androidx.privacysandbox.tools.core.generator.build
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.ucbp1.features.movie.domain.model.MovieModel

/**
 * Vista Composable que muestra una cuadrícula de películas populares.
 * Esta función es llamada por PopularMoviesScreen.
 */
@Composable
fun PopularMoviesView(
    movies: List<MovieModel>,
    onLikeClicked: (movieId: Int) -> Unit,
    onMovieCardClicked: (movie: MovieModel) -> Unit, // Para navegar a detalles, etc.
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) {
        // Podrías mostrar un mensaje aquí si la lista está vacía después de cargar,
        // pero el PopularMoviesScreen ya maneja el estado de "no hay películas".
        // Esta vista se enfoca solo en renderizar la lista si existe.
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),   // Espaciado vertical
        horizontalArrangement = Arrangement.spacedBy(10.dp), // Espaciado horizontal
        contentPadding = PaddingValues(all = 10.dp),         // Padding alrededor de la cuadrícula
        modifier = modifier.fillMaxSize()
    ) {
        items(movies, key = { it.id }) { movie -> // Usar la movie.id como key
            CardMovie(
                movie = movie,
                onLikeClicked = { onLikeClicked(movie.id) },
                onCardClicked = { onMovieCardClicked(movie) }
            )
        }
    }
}

@Composable
fun CardMovie(
    movie: MovieModel,
    onLikeClicked: () -> Unit,
    onCardClicked: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            // .padding(4.dp) // El padding general se maneja en LazyVerticalGrid
            .fillMaxSize() // La Card debería intentar llenar el espacio de la celda
            .clickable { onCardClicked() }, // Hacer toda la tarjeta clickeable
        shape = MaterialTheme.shapes.medium, // Usar formas del tema
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            // No es necesario fillMaxSize aquí si OutlinedCard ya lo hace,
            // pero sí para el contenido interno si quieres que se expanda.
            modifier = Modifier.fillMaxWidth(), // Para que el contenido se expanda horizontalmente
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopEnd) { // Box para superponer el botón de like
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterUrl ?: "https://via.placeholder.com/200x300?text=No+Image")
                        //.crossfade(true)
                        // .placeholder(R.drawable.placeholder_image) // Tus placeholders
                        // .error(R.drawable.error_image)
                        .build(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f), // Relación de aspecto común para pósters
                    contentScale = ContentScale.Crop
                )
                // Botón de Like superpuesto
                IconButton(
                    onClick = onLikeClicked,
                    modifier = Modifier.padding(4.dp) // Pequeño padding para el botón
                ) {
                    Icon(
                        imageVector = if (movie.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (movie.isLiked) "Quitar de favoritos" else "Añadir a favoritos",
                        tint = if (movie.isLiked) Color.Red else Color.White // Ajusta colores según tu tema
                    )
                }
            }

            Text(
                text = movie.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall, // Estilo apropiado
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth()
            )
            // Opcional: Mostrar una pequeña parte de la descripción o fecha de lanzamiento
            // Text(
            //     text = movie.releaseDate ?: "",
            //     textAlign = TextAlign.Center,
            //     style = MaterialTheme.typography.bodySmall,
            //     maxLines = 1,
            //     modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).fillMaxWidth()
            // )
        }
    }
}

