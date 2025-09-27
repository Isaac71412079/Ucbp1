package com.example.ucbp1.features.profile.domain.model.value

@JvmInline
value class ProfileEmail(val value: String) {
    init {
        require(value.contains("@")) { "Email no válido! $value" }
    }
}