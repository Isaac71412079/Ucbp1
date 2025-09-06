package com.example.ucbp1.features.github.data.repository

import com.example.ucbp1.features.github.domain.model.UserModel
import com.example.ucbp1.features.github.domain.repository.IGithubRepository

class GithubRepository: IGithubRepository {
    override fun findByNick(value: String): Result<UserModel> {
        if(value.isEmpty()) {
            return Result.failure(Exception("El campo no puede estar vacio"))
        }
        return Result.success(
            UserModel(
                "calyr",
                "https://avatars.githubusercontent.com/u/874321?v=4"
            )
        )

    }
}