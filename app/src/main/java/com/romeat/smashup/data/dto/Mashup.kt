package com.romeat.smashup.data.dto

@kotlinx.serialization.Serializable
data class Mashup(
    val id: Int,
    val name: String,
    val owner: String,
    val imageUrl: String,
    val explicit: Boolean,
    val bitrate: Int,
    val streams: Int,
    val likes: Int,
    val tracks: List<Int>
)
