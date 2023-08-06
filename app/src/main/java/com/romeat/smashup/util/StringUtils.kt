package com.romeat.smashup.util

import java.util.concurrent.TimeUnit

fun Long.toDisplayableTimeString(): String {
    if (this <= 0) return "00:00"

    val HH = TimeUnit.MILLISECONDS.toHours(this)
    val MM = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val SS = TimeUnit.MILLISECONDS.toSeconds(this) % 60

    return if (HH > 0) {
        String.format("%d:%02d:%02d", HH, MM, SS)
    } else {
        String.format("%02d:%02d", MM, SS)
    }
}

fun Int.toStringWithThousands(): String {
    return if(this < 1000) {
        this.toString()
    } else {
        "%.1f".format(this.toDouble()/1000) + "K"
    }
}

fun ConcatAuthorAndTitle(author: String, title: String): String {
    if (author.isBlank() && title.isBlank()) return ""
    return "$author - $title"
}

fun String.addEmptyLines(lines: Int) = this + "\n".repeat(lines)