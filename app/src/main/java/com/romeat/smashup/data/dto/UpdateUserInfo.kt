package com.romeat.smashup.data.dto

import com.google.gson.annotations.SerializedName

data class ChangeEmailRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val newEmail: String
)

data class ChangePasswordRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("newPassword")
    val newPassword: String
)

data class ChangeUsernameRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val newUsername: String
)

data class UpdateAvatarRequest(
    @SerializedName("basedImageFile")
    val base64Image: String
)