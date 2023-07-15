package com.romeat.smashup.data.dto

@kotlinx.serialization.Serializable
data class Mashup(
    val id: Int,
    val name: String,
    val authors: List<String>,
    val genres: List<String>,
    val tracks: List<Int>,
    val imageUrl: String,
    val statuses: Int,
    val albumId: Int,
    val likes: Int,
    val streams: Int,
    val bitrate: Int,
    val duration: Long,
) {
    fun isExplicit(): Boolean = statuses == 0

    val owner: String
        get() = authors.joinToString(", ")
}

fun Mashup.toMashupUListItem(): MashupListItem {
    return MashupListItem(
        id = this.id,
        name = this.name,
        owner = this.owner,
        imageUrl = this.imageUrl,
        explicit = this.isExplicit(),
        streams = this.streams,
        likes = this.likes,
        tracks = this.tracks,
        isLiked = false
    )
}
