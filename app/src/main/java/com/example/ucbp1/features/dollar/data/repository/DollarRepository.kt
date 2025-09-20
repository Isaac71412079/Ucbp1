package com.example.ucbp1.features.dollar.data.repository

import com.example.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.example.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.data.mapper.toEntity
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class DollarRepository(
    val realTimeRemoteDataSource: RealTimeRemoteDataSource,
    val localDataSource: DollarLocalDataSource
): IDollarRepository {
    override suspend fun getDollarUpdates(): Flow<Dollar> {
        return realTimeRemoteDataSource.getDollarUpdates()
            .onEach {
                localDataSource.insert(it)
            }
    }
}
