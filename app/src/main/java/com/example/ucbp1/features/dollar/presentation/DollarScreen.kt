package com.example.ucbp1.features.dollar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun DollarScreen(
    viewModelDollar: DollarViewModel = koinViewModel()
) {
    val state by viewModelDollar.uiState.collectAsState()

    // --- CAMBIO 1: Usar Column de Compose en lugar de TableInfo.Column de Room ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val stateValue = state) {
            is DollarViewModel.DollarUIState.Loading -> CircularProgressIndicator()
            is DollarViewModel.DollarUIState.Error -> Text(
                text = stateValue.message,
                color = MaterialTheme.colorScheme.error
            )
            is DollarViewModel.DollarUIState.Success -> {
                // --- CAMBIO 2: Ahora 'dollar' es un objeto Dollar, por lo que el código es válido ---
                val dollar = stateValue.data

                DollarCard(
                    title = "Dólar Oficial",
                    buy = dollar.officialBuy,
                    sell = dollar.officialSell
                )

                Spacer(modifier = Modifier.height(16.dp))

                DollarCard(
                    title = "Dólar Paralelo",
                    buy = dollar.parallelBuy,
                    sell = dollar.parallelSell
                )

                dollar.lastUpdated?.let { timestamp ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Última actualización: ${formatDate(timestamp)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun DollarCard(title: String, buy: String?, sell: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Compra",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$ ${buy ?: "--"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Venta",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$ ${sell ?: "--"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return try {
        sdf.format(java.util.Date(timestamp))
    } catch (e: Exception) {
        "Fecha inválida"
    }
}
