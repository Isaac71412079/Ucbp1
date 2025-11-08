package com.example.ucbp1.features.dollar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun DollarScreen(
    viewModelDollar: DollarViewModel = koinViewModel()
) {
    val state by viewModelDollar.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val stateValue = state) {
            DollarViewModel.DollarUIState.Loading -> CircularProgressIndicator()
            is DollarViewModel.DollarUIState.Error -> Text(
                text = stateValue.message,
                color = MaterialTheme.colorScheme.error
            )
            is DollarViewModel.DollarUIState.Success -> {
                val dollar = stateValue.data

                if (dollar == null) {
                    Text("Obteniendo datos del dólar...")
                } else {
                    // --- Card Principal (se muestra siempre) ---
                    DollarCard(
                        title = "Dólar Oficial",
                        buy = dollar.officialBuy,
                        sell = dollar.officialSell
                    )

                    DollarCard(
                        title = "Dólar Paralelo",
                        buy = dollar.parallelBuy,
                        sell = dollar.parallelSell
                    )

                    // --- NUEVO: Card de Variación (se muestra solo si hay datos) ---
                    if (dollar.officialSellVariation != null || dollar.parallelSellVariation != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Variación", style = MaterialTheme.typography.titleMedium)
                        VariationCard(title = "Oficial", variation = dollar.officialSellVariation)
                        VariationCard(title = "Paralelo", variation = dollar.parallelSellVariation)
                    }

                    dollar.lastUpdated?.let { timestamp ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Última actualización: ${formatDate(timestamp)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

// --- INICIO: CÓDIGO RESTAURADO Y CORREGIDO ---
// Este es el Composable que faltaba. Lo he restaurado desde tu código original.
@Composable
fun DollarCard(title: String, buy: String?, sell: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Padding vertical reducido
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding unificado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título centrado
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )

            // Row para alinear Compra y Venta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround, // SpaceAround para mejor distribución
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Compra",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = buy ?: "--",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Venta",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = sell ?: "--",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
// --- FIN: CÓDIGO RESTAURADO Y CORREGIDO ---


// --- NUEVO COMPOSABLE PARA EL CARD DE VARIACIÓN ---
@Composable
fun VariationCard(title: String, variation: Double?) {
    if (variation == null) return

    val (icon, color, text) = when {
        variation > 0.00001 -> Triple(Icons.Default.ArrowUpward, Color(0xFFD32F2F), "Subió")
        variation < -0.00001 -> Triple(Icons.Default.ArrowDownward, Color(0xFF388E3C), "Bajó")
        else -> Triple(Icons.Default.DragHandle, Color.Gray, "Estable")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text, style = MaterialTheme.typography.bodyLarge, color = color)
                Icon(imageVector = icon, contentDescription = text, tint = color)
                Text(
                    text = "%+.4f".format(variation),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

