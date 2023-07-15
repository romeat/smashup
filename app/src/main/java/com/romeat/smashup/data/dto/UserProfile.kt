package com.romeat.smashup.data.dto

data class UserProfile(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val permissions: Int,
    val mashups: List<Int>,
    val playlists: List<Int>
)