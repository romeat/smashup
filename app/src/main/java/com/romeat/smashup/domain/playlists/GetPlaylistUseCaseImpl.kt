package com.romeat.smashup.domain.playlists

import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetPlaylistUseCaseImpl @Inject constructor(
    private val remoteData: SmashupRemoteData
) : GetPlaylistUseCase {
    override suspend fun invoke(ids: List<Int>): Flow<Resource<List<Playlist>>> =
         getResourceWithExceptionLogging<List<Playlist>>(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getPlaylists(ids)
                if (response.isSuccessful) {
                    response.body()!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}