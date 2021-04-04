package com.example.breakingBad.data.models.character


import com.squareup.moshi.Json

data class Episode(
    @Json(name = "air_date")
    val airDate: String,
    @Json(name = "characters")
    val characters: List<String>,
    @Json(name = "episode")
    val episode: Int,
    @Json(name = "episode_id")
    val episodeId: Int,
    @Json(name = "season")
    val season: Int,
    @Json(name = "title")
    val title: String
)