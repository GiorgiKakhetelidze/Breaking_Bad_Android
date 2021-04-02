package com.example.breakingBad.data.models.user


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserRegistrationRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "userName")
    val userName: String
)