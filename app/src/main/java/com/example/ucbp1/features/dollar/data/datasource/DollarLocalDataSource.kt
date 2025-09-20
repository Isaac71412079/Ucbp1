package com.example.ucbp1.features.dollar.data.datasource

import com.example.ucbp1.features.dollar.data.database.dao.IDollarDao
import com.example.ucbp1.features.dollar.data.mapper.toEntity
import com.example.ucbp1.features.dollar.data.mapper.toModel
import com.example.ucbp1.features.dollar.domain.model.Dollar

class DollarLocalDataSource(
    val dao: IDollarDao
) {

    suspend fun getList(): List<Dollar> {
        return dao.getList().map {
            it.toModel()
        }

    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    suspend fun inserTDollars(list: List<Dollar>) {
        val dollarEntity = list.map { it.toEntity() }
        dao.insertDollars(dollarEntity)
    }

    suspend fun insert(dollar: Dollar) {
        dao.insert(dollar.toEntity())
    }

}