package com.example.ucbp1.features.profile.usecase

import com.example.ucbp1.features.profile.domain.model.ProfileModel
import com.example.ucbp1.features.profile.domain.model.value.*
import com.example.ucbp1.features.profile.domain.repository.IProfileRepository
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test


class GetProfileUseCaseTest {
    private val fakeRepository = object : IProfileRepository {
        override fun fetchData() = flowOf(
            Result.success(
                ProfileModel(
                    id = ProfileId("user01"),
                    name = ProfileName("Isaac"),
                    email = ProfileEmail("isaac@ucb.edu.bo"),
                    avatarUrl = ProfilePathUrl(null)
                )
            )
        )
    }

    @Test
    fun `GetProfileUseCase retorna perfil exitoso`() = runBlocking {
        val useCase = GetProfileUseCase(fakeRepository)
        val result = useCase().first() // recolectamos el primer valor
        assertTrue(result.isSuccess)
        assertEquals("Isaac", result.getOrNull()?.name?.value)
    }
}