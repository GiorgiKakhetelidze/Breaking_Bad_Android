package com.example.breakingBad.data.network

import com.example.breakingBad.data.models.user.UserProfile
import com.example.breakingBad.data.models.user.UserRegistrationRequest
import com.example.breakingBad.data.models.user.UserSession
import retrofit2.http.*

interface UserService {

    @GET("/auth/user")
    suspend fun getUser(): UserProfile

    @GET("/user/braking-bad/get-my-characters")
    suspend fun getUserCharacters(): List<Int>

    @POST("/auth/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): UserSession

    @POST("/auth/register")
    suspend fun register(@Body request: UserRegistrationRequest)

    @POST("/user/braking-bad/save-character")
    suspend fun saveUserCharacter(@Query("characterId") characterId: Int)

    @DELETE("/user/braking-bad/delete-my-character")
    suspend fun deleteUserCharacter(@Query("characterId") characterId: Int)
}