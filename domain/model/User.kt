package com.example.composemaster.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val avatarUrl: String? = null
)
