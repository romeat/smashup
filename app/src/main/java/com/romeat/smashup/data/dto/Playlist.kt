package com.romeat.smashup.data.dto

data class Playlist(
    val id: Int,
    val name: String,
    val owner: String,
    val imageUrl: String,
    val description: String,
    val streams: Int,
    val likes: Int,
    val mashups: List<Int>
)
