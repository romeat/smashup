package com.romeat.smashup.data.dto

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val authors: List<String>,
    val imageUrl: String,
    // val type: String - ?
    val mashups: List<Int>,
    val streams: Int,
    val likes: Int,
) {
    val owner: String
        get() = authors.joinToString(", ")
}
