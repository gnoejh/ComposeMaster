package com.example.composemaster.data.remote.model

import com.example.composemaster.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("avatar")
    val avatarUrl: String?
) {
    fun toUser(): User = User(id, name, email, avatarUrl)
}
