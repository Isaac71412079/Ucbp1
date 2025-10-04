package com.example.ucbp1.features.movie.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
//import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    stars: Int = 5,
    onRatingChanged: (Int) -> Unit
) {
    Row(modifier = modifier) {
        for (starIndex in 1..stars) {
            val isSelected = starIndex <= rating
            val newRating = if (isSelected && rating == starIndex) 0 else starIndex

            Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $starIndex",
                tint = if (isSelected) Color(0xFFFFD700) else Color.Gray, // Amarillo dorado
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onRatingChanged(newRating) }
            )
        }
    }
}
