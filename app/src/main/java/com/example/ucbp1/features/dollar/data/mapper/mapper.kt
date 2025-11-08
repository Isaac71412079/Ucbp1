package com.example.ucbp1.features.dollar.data.mapper

import com.example.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.example.ucbp1.features.dollar.domain.model.Dollar

fun DollarEntity.toModel() : Dollar {
    return Dollar(
        officialBuy = officialBuy,
        officialSell = officialSell,
        parallelBuy = parallelBuy,
        parallelSell = parallelSell,
        lastUpdated = timestamp
    )
}

fun Dollar.toEntity() : DollarEntity {
    return DollarEntity(
        officialBuy = officialBuy,
        officialSell = officialSell,
        parallelBuy = parallelBuy,
        parallelSell = parallelSell,
        timestamp = lastUpdated ?: System.currentTimeMillis()
    )
}

// Este mapper convierte la lista de 2 entidades a un único modelo de dominio con la variación.
fun List<DollarEntity>.toModelWithVariation(): Dollar? {
    if (this.isEmpty()) return null

    val currentEntity = this[0]
    val previousEntity = this.getOrNull(1)

    // Calculamos la variación entre el valor de venta actual y el previo.
    val officialSellVariation = calculateVariation(currentEntity.officialSell, previousEntity?.officialSell)
    val parallelSellVariation = calculateVariation(currentEntity.parallelSell, previousEntity?.parallelSell)

    return Dollar(
        officialBuy = currentEntity.officialBuy,
        officialSell = currentEntity.officialSell,
        parallelBuy = currentEntity.parallelBuy,
        parallelSell = currentEntity.parallelSell,
        lastUpdated = currentEntity.timestamp,
        // Asignamos las variaciones calculadas
        officialSellVariation = officialSellVariation,
        parallelSellVariation = parallelSellVariation
    )
}

// Función de ayuda para calcular la diferencia de forma segura.
private fun calculateVariation(current: String?, previous: String?): Double? {
    val currentValue = current?.toDoubleOrNull()
    val previousValue = previous?.toDoubleOrNull()
    return if (currentValue != null && previousValue != null) {
        currentValue - previousValue
    } else {
        null
    }
}