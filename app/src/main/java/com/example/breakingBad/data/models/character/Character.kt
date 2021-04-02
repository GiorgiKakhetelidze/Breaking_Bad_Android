package com.example.breakingBad.data.models.character


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Character(
    @Json(name = "appearance")
    val appearance: List<Int>,
    @Json(name = "better_call_saul_appearance")
    val betterCallSaulAppearance: List<Int>,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "char_id")
    @PrimaryKey
    val charId: Int,
    @Json(name = "img")
    val img: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "occupation")
    val occupation: List<String>,
    @Json(name = "portrayed")
    val portrayed: String,
    @Json(name = "status")
    val status: String
) : Parcelable