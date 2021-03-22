package com.example.breakingBad.data.network

import com.example.breakingBad.data.models.character.Character
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {

    @GET("characters")
    suspend fun getAllCharacters(): MutableList<Character>

    @GET("characters")
    suspend fun getLimitedCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MutableList<Character>

    @GET("characters")
    suspend fun getCharacterByName(@Query("name") name : String) : MutableList<Character>
}