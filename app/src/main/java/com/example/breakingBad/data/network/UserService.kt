package com.example.breakingBad.data.network

import com.example.breakingBad.data.models.user.UserProfile
import com.example.breakingBad.data.models.user.UserRegistrationRequest
import com.example.breakingBad.data.models.user.UserSession
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("/auth/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): UserSession

    @GET("/auth/user")
    suspend fun getUser(): UserProfile

    @GET("/user/braking-bad/get-my-characters")
    suspend fun getUserCharacters(): List<Int>

    @POST("/auth/register")
    suspend fun register(@Body request: UserRegistrationRequest)
}