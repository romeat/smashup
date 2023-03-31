package com.romeat.smashup.data.dto

data class OwnProfile(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val permissions: Int,
    val mashups: List<Int>,
    val playlists: List<Int>,
    val mashupsLikes: List<Int>,
    val playlistsLikes: List<Int>
)