package com.example.ucbp1.domain.usecase

import com.example.ucbp1.domain.model.UserModel
import com.example.ucbp1.domain.repository.IGithubRepository

class FindByNicknameUseCase (
    val repository: IGithubRepository
) {
    fun invoke( nickname: String) : Result<UserModel> {
        return repository.findByNick(nickname)
    }
}