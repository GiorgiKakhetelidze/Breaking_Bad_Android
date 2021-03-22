package com.example.breakingBad.data.models.user


import com.squareup.moshi.Json

data class UserProfile(
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "userName")
    val userName: String
)