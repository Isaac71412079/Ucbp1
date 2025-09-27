package com.example.ucbp1.features.profile.domain.model

import com.example.ucbp1.features.profile.domain.model.value.ProfileId
import com.example.ucbp1.features.profile.domain.model.value.ProfileEmail
import com.example.ucbp1.features.profile.domain.model.value.ProfilePathUrl
import com.example.ucbp1.features.profile.domain.model.value.ProfileName


data class ProfileModel(
    val id: ProfileId,
    val name: ProfileName,
    val email: ProfileEmail,
    val avatarUrl: ProfilePathUrl = ProfilePathUrl(null)
)