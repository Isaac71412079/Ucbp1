package com.example.ucbp1.features.profile.domain.usecase

import com.example.ucbp1.features.profile.domain.model.ProfileModel
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(
    private val repository: IProfileRepository
) {
    operator fun invoke(): Flow<Result<ProfileModel>> {
        return repository.fetchData()
    }
}