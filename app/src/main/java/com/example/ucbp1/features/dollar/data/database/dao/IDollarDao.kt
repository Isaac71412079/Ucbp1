package com.example.ucbp1.features.dollar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.google.firebase.database.PropertyName
import kotlinx.coroutines.flow.Flow // <-- **Asegúrate de importar Flow**

@Dao
interface IDollarDao {
    @Query("SELECT * FROM dollars")
    suspend fun getList(): List<DollarEntity>

    // --- INICIO: FUNCIÓN AÑADIDA ---
    // Esta consulta nos da las 2 últimas entradas ordenadas por tiempo.
    // Devuelve un Flow para que la UI se actualice automáticamente.
    @Query("SELECT * FROM dollars ORDER BY timestamp DESC LIMIT 2")
    fun getLatestTwoRates(): Flow<List<DollarEntity>>
    // --- FIN: FUNCIÓN AÑADIDA ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dollar: DollarEntity)

    @Query("DELETE FROM dollars")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDollars(lists: List<DollarEntity>)
}

data class Dollar(
    @get:PropertyName("official_buy")
    @set:PropertyName("official_buy")
    var officialBuy: String? = null,

    @get:PropertyName("official_sell")
    @set:PropertyName("official_sell")
    var officialSell: String? = null,

    @get:PropertyName("parallel_buy")
    @set:PropertyName("parallel_buy")
    var parallelBuy: String? = null,

    @get:PropertyName("parallel_sell")
    @set:PropertyName("parallel_sell")
    var parallelSell: String? = null,

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var lastUpdated: Long? = null,

    // --- INICIO: CAMPOS AÑADIDOS PARA LA VARIACIÓN ---
    // Se calculan localmente, no afectan a Firebase ni a la BD local.
    var officialSellVariation: Double? = null,
    var parallelSellVariation: Double? = null
    // --- FIN: CAMPOS AÑADIDOS ---
)