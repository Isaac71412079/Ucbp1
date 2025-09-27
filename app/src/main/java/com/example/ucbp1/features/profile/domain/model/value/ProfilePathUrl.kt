package com.example.ucbp1.features.profile.domain.model.value

@JvmInline
value class ProfilePathUrl (val value: String?){
    fun orDefault(): String = value ?: "https://example.com/default-avatar.png"
}
