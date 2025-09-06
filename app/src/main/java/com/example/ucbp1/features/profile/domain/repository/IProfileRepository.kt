package com.example.ucbp1.features.profile.domain.repository

import com.example.ucbp1.features.profile.domain.model.ProfileModel

interface IProfileRepository {
    fun fetchData(): Result<ProfileModel>
}