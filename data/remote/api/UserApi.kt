package com.example.composemaster.data.remote.api

import com.example.composemaster.data.remote.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<UserResponse>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): UserResponse
}
