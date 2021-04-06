package com.example.breakingBad.data.models.user


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Keep
@Entity
data class UserProfile(
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "userName")
    val userName: String,
    @PrimaryKey
    val id: Int = 1
)