package com.romeat.smashup.data.dto

@kotlinx.serialization.Serializable
data class LoginResponse(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val backgroundColor: Int,
    val permissions: Int,
    val token: String
)
