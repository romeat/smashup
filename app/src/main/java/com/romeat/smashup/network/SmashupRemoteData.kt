package com.romeat.smashup.network

import javax.inject.Inject

class SmashupRemoteData @Inject constructor(
    private val mainService : MainService
) {

    suspend fun getUserInfo(username: String) = mainService.getCurrentUser(username)

    suspend fun getAuthorInfo(name: String) = mainService.getAuthorProfile(name)

    suspend fun getMashupsList(ids: List<Int>) = mainService.getMashupsList(ids.joinToString(","))

    suspend fun getSourcesList(ids: List<Int>) = mainService.getSourcesList(ids.joinToString(","))

    suspend fun getMashupsWithSource(id: Int) = mainService.getMashupsWithSource(id)

    suspend fun getCompilationsIds() = mainService.getCompilationsIds()

    suspend fun getPlaylists(ids: List<Int>) = mainService.getPlaylists(ids.joinToString(","))


    suspend fun getMashupsWithName(searchQuery: String) = mainService.getMashupsWithName(searchQuery)

    suspend fun getPlaylistsWithName(searchQuery: String) = mainService.getPlaylistsWithName(searchQuery)

    suspend fun getSourcesWithName(searchQuery: String) = mainService.getSourcesWithName(searchQuery)

    suspend fun getUsersWithName(searchQuery: String) = mainService.getUsersWithName(searchQuery)
}