package com.example.ucbp1.features.dollar.domain.repository

import com.example.ucbp1.features.dollar.domain.model.Dollar
import kotlinx.coroutines.flow.Flow

interface IDollarRepository {
    fun getDollarUpdates(): Flow<Result<Dollar>>
}