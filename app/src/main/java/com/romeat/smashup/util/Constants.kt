package com.romeat.smashup.util

object Constants {
    const val LOG_TAG = "SMASHUP"
}

object MediaConstants {
    // music player consts

    const val STOP_PLAYER = "STOP_GODDAMN_PLAYER"
    const val MASHUP_TO_PLAY = "MASHUP_TO_PLAY"
    const val PLAYLIST_TO_PLAY = "PLAYLIST_TO_PLAY"
}

object CommonNavigationConstants {
    const val PLAYLIST_ROUTE = "PLAYLIST"
    const val PLAYLIST_PARAM = "playlistId"

    const val PLAYLIST_LIST_ROUTE = "PLAYLISTS"
    const val PLAYLIST_LIST_PARAM = "playlistIds"

    const val MASHUP_ROUTE = "MASHUP"
    const val MASHUP_PARAM = "mashupId"

    const val AUTHOR_ROUTE = "AUTHOR"
    const val AUTHOR_PARAM = "authorAlias"

    const val SOURCE_ROUTE = "SOURCE"
    const val SOURCE_PARAM = "sourceId"
}

object LoginFlow {
    const val MinUsernameLength = 4
    const val MaxUsernameLength = 32

    const val MinPasswordLength = 8
    const val MaxPasswordLength = 32

    val UsernameRegex = "(?=^[а-яА-ЯёЁa-zA-Z0-9_ ]{4,32}\$)(?!^\\d+\$)^.+\$".toRegex()
    val PasswordRegex = "[a-zA-Z0-9-_=+()*&^%\$#@!]{8,32}".toRegex()
}

object AuthNavigationConstants {
    const val EMAIL_PARAM = "email"
}