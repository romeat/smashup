package com.romeat.smashup.data.dto

import com.google.gson.annotations.SerializedName

data class SmashupSettings(
    @SerializedName("settings")
    val bits: Int,
)
