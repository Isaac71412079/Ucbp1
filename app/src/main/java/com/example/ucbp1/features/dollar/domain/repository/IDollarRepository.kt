package com.example.ucbp1.features.dollar.domain.repository

import com.example.ucbp1.features.dollar.domain.model.Dollar
import kotlinx.coroutines.flow.Flow

interface IDollarRepository {
    // Función para que la UI observe los datos locales
    fun getDollarFromLocal(): Flow<Dollar?>

    // Función para iniciar la sincronización en segundo plano
    suspend fun syncFirebaseToLocal()
}