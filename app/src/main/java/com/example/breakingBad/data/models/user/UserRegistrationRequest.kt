package com.example.breakingBad.data.models.user


import com.squareup.moshi.Json

data class UserRegistrationRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "userName")
    val userName: String
)