package com.example.ucbp1.features.profile.domain.repository

import com.example.ucbp1.features.profile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun fetchData(): Flow<Result<ProfileModel>>
}