package com.example.ucbp1.features.profile.domain.model.value

@JvmInline
value class ProfileName(val value: String) {
    init {
        require(value.isNotBlank()) { "El nombre no debe estar vacío!" }
    }
}

