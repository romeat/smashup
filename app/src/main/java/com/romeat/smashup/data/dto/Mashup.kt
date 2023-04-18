package com.romeat.smashup.data.dto

@kotlinx.serialization.Serializable
data class Mashup(
    val id: Int,
    val name: String,
    val owner: String,
    val imageUrl: String,
    val explicit: Boolean,
    val streams: Int,
    val likes: Int,
    val tracks: List<Int>
)

fun Mashup.toMashupUListItem(): MashupListItem {
    return MashupListItem(
        id = this.id,
        name = this.name,
        owner = this.owner,
        imageUrl = this.imageUrl,
        explicit = this.explicit,
        streams = this.streams,
        likes = this.likes,
        tracks = this.tracks,
        isLiked = false
    )
}
