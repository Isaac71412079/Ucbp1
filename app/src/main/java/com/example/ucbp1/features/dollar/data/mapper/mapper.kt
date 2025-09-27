package com.example.ucbp1.features.dollar.data.mapper

import com.example.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.example.ucbp1.features.dollar.domain.model.Dollar

fun DollarEntity.toModel() : Dollar {
    return Dollar(
        official = official,
        parallel = parallel
    )
}

fun Dollar.toEntity() : DollarEntity {
    return DollarEntity(
        official = official,
        parallel = parallel)
}