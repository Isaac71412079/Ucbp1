package com.example.ucbp1.features.dollar.domain.usecase

import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class GetDollarUseCase(
    val dollarRepository: IDollarRepository
) {
    suspend fun invoke(): Flow<Dollar> {
        return dollarRepository.getDollarUpdates()
    }
}
