package com.example.ucbp1.features.profile.data.repository

import com.example.ucbp1.features.profile.domain.model.ProfileModel
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import com.example.ucbp1.features.profile.domain.model.value.ProfileId
import com.example.ucbp1.features.profile.domain.model.value.ProfileEmail
import com.example.ucbp1.features.profile.domain.model.value.ProfilePathUrl
import com.example.ucbp1.features.profile.domain.model.value.ProfileName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class ProfileRepository: IProfileRepository {
    override fun fetchData(): Flow<Result<ProfileModel>> = flow {
        // Simulamos datos de perfil por ahora

        val profile = ProfileModel(
            id = ProfileId("user01"),
            name = ProfileName("Isaac"),
            email = ProfileEmail("isaac.rivero@ucb.edu.bo"),
            avatarUrl = ProfilePathUrl("https://img.freepik.com/vector-gratis/capas-fondo-abstractas-colores_23-2148460310.jpg?semt=ais_hybrid&w=740&q=80")
        )
        emit(Result.success(profile))
    }
}