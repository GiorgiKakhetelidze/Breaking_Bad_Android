package com.example.breakingBad.data.models.user


import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserProfile(
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "userName")
    val userName: String
)