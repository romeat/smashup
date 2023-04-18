package com.romeat.smashup.data.dto

data class MashupListItem(
    val id: Int,
    val name: String,
    val owner: String,
    val imageUrl: String,
    val explicit: Boolean,
    val streams: Int,
    val likes: Int,
    val tracks: List<Int>,
    val isLiked: Boolean
)