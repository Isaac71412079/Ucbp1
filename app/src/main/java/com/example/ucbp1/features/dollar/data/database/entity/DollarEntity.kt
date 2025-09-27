package com.example.ucbp1.features.dollar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dollars")
data class DollarEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "official_buy")
    var officialBuy: String? = null,

    @ColumnInfo(name = "official_sell")
    var officialSell: String? = null,

    @ColumnInfo(name = "parallel_buy")
    var parallelBuy: String? = null,

    @ColumnInfo(name = "parallel_sell")
    var parallelSell: String? = null,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
)