package com.romeat.smashup.data.dto

data class Source(
    val id: Int,
    val name: String,
    val authors: List<String>,
    val imageUrl: String,
    val link: String = "",
    val backgroundColor: Int = 0,
) {
    val owner: String
        get() = authors.joinToString(", ")
}
