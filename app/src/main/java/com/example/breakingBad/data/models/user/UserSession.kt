package com.example.breakingBad.data.models.user


import com.squareup.moshi.Json

data class UserSession(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "userName")
    val userName: String
)