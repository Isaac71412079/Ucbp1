package com.example.ucbp1.features.github.domain.usecase

import com.example.ucbp1.features.github.domain.model.UserModel
import com.example.ucbp1.features.github.domain.repository.IGithubRepository
import kotlinx.coroutines.delay

class FindByNicknameUseCase(
    val repository: IGithubRepository
) {
    suspend fun invoke(nickname: String) : Result<UserModel>  {
        delay(2000)
        return repository.findByNick(nickname)
    }
}