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
