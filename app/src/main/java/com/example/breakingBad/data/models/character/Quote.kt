package com.example.breakingBad.data.models.character

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class Quote(
    @Json(name = "author")
    val author: String,
    @Json(name = "quote")
    val quote: String,
    @Json(name = "quote_id")
    val quoteId: Int,
    @Json(name = "series")
    val series: String
)