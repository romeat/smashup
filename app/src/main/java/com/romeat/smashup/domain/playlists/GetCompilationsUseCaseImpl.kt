package com.romeat.smashup.domain.playlists

import android.util.Log
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

/*
class GetCompilationsUseCaseImpl @Inject constructor(
    private val remoteData: SmashupRemoteData
) : GetCompilationsUseCase {
    override suspend fun invoke(): Flow<Resource<List<Playlist>>> =
        getResourceWithExceptionLogging<List<Playlist>>(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getCompilationsIds()
                if (response.isSuccessful) {
                    val playlistsResponse = remoteData.getPlaylists(response.body()!!)
                    if (playlistsResponse.isSuccessful) {
                        playlistsResponse.body()!!
                    } else {
                        throw HttpException(playlistsResponse)
                    }
                } else {
                    throw HttpException(response)
                }
            }
        )
}

 */
