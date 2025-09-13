package com.example.ucbp1.features.github.data.api.dto

import com.google.gson.annotations.SerializedName

data class GithubDto(val login: String,
                     @SerializedName("avatar_url") val url: String,
                     val name: String?,
                     val company: String?,
                     val bio: String?)
