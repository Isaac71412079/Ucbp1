package com.example.ucbp1.features.dollar.data.repository

import com.example.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.example.ucbp1.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.ucbp1.features.dollar.data.mapper.toModelWithVariation // Importa el nuevo mapper
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // Importa el operador map

class DollarRepository(
    val realTimeRemoteDataSource: RealTimeRemoteDataSource,
    val localDataSource: DollarLocalDataSource
): IDollarRepository {

    // Esta función SÓLO lee de la base de datos local y la transforma para la UI.
    // Es rápida y devuelve un Flow que se actualiza solo.
    override fun getDollarFromLocal(): Flow<Dollar?> {
        return localDataSource.getLatestTwo()
            .map { entityList ->
                entityList.toModelWithVariation()
            }
    }

    // Esta función SÓLO escucha a Firebase y guarda en la base de datos.
    // Es una función 'suspend' que se ejecuta en segundo plano.
    override suspend fun syncFirebaseToLocal() {
        realTimeRemoteDataSource.getDollarUpdates().collect { dollarFromFirebase ->
            localDataSource.insert(dollarFromFirebase)
        }
    }
}
