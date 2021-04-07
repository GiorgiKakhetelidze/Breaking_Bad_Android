package com.example.breakingBad.data.network

import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.data.models.character.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    @GET("characters")
    suspend fun getAllCharacters(): MutableList<Character>

    @GET("characters")
    suspend fun getLimitedCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<Character>

    @GET("characters")
    suspend fun getCharacterByName(
        @Query(
            "name",
            encoded = true
        ) name: String
    ): List<Character>

    @GET("characters/{id}")
    suspend fun getCharacterBydId(
        @Path("id") id: Int,
    ): MutableList<Character>

    @GET("quotes")
    suspend fun getAllQuotes(): MutableList<Quote>

    @GET("quote")
    suspend fun getQuotesByAuthor(
        @Query("author", encoded = true) author: String
    ): MutableList<Quote>

    @GET("episodes")
    suspend fun getEpisodesBySeries(
        @Query("series", encoded = true) episode: String
    ): MutableList<Episode>
}