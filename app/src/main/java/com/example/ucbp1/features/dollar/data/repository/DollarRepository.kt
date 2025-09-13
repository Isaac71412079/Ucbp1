package com.example.ucbp1.features.dollar.data.repository

import com.example.ucbp1.features.dollar.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DollarRepository(
    private val realTimeRemoteDataSource: RealTimeRemoteDataSource
) : IDollarRepository {

    override fun getDollarUpdates(): Flow<Result<Dollar>> {
        return realTimeRemoteDataSource.getDollarUpdates()
    }
}