package com.example.ucbp1.features.dollar.domain.model

data class Dollar(
    @get:com.google.firebase.database.PropertyName("official_buy")
    @set:com.google.firebase.database.PropertyName("official_buy")
    var officialBuy: String? = null,

    @get:com.google.firebase.database.PropertyName("official_sell")
    @set:com.google.firebase.database.PropertyName("official_sell")
    var officialSell: String? = null,

    @get:com.google.firebase.database.PropertyName("parallel_buy")
    @set:com.google.firebase.database.PropertyName("parallel_buy")
    var parallelBuy: String? = null,

    @get:com.google.firebase.database.PropertyName("parallel_sell")
    @set:com.google.firebase.database.PropertyName("parallel_sell")
    var parallelSell: String? = null,

    @get:com.google.firebase.database.PropertyName("timestamp")
    @set:com.google.firebase.database.PropertyName("timestamp")
    var lastUpdated: Long? = null,

    // --- INICIO: CAMPOS AÑADIDOS PARA LA VARIACIÓN ---
    // Se calculan localmente, no afectan a Firebase ni a la BD local.
    var officialSellVariation: Double? = null,
    var parallelSellVariation: Double? = null
    // --- FIN: CAMPOS AÑADIDOS ---
)
