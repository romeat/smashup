package com.romeat.smashup.domain.playlists

import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GetPlaylistUseCase {
    suspend operator fun invoke(ids: List<Int>): Flow<Resource<List<Playlist>>>
}