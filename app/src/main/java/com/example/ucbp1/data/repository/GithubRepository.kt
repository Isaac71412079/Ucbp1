package com.example.ucbp1.data.repository

import com.example.ucbp1.domain.model.UserModel
import com.example.ucbp1.domain.repository.IGithubRepository

class GithubRepository : IGithubRepository{
    override fun findByNick(value: String): Result<UserModel> {
        return Result.success(UserModel("example", "https://avatars.githubusercontent.com/u/36901?v=4"))
    }

}