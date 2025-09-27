package com.example.ucbp1.features.dollar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dollars")
data class DollarEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "official")
    var official: String? = null,

    @ColumnInfo(name = "parallel")
    var parallel: String? = null,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0)