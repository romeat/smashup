package com.romeat.smashup.presentation.home.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.presentation.home.MusicServiceViewModel
import com.romeat.smashup.presentation.home.PlaylistTitle
import com.romeat.smashup.util.PlaylistsToSquareDisplayItems
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SquareDisplayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    //private val compilationsUseCase: GetCompilationsUseCase
    private val getPlaylistUseCase: GetPlaylistUseCase,
    musicServiceConnection: MusicServiceConnection
) : MusicServiceViewModel(musicServiceConnection) {

    var state by mutableStateOf(ChartsState())

    init {
        playlistTitle = PlaylistTitle.ResType(R.string.playlist_recommendations)

        viewModelScope.launch {
            //compilationsUseCase
            //    .invoke()
            getPlaylistUseCase
                .invoke(listOf(1, 2))
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(
                                    playlists = PlaylistsToSquareDisplayItems(result.data),
                                    isLoading = false,
                                    isError = false
                                )
                            }
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = result.message ?: "some error"
                            )
                        }
                    }
                }
        }
    }
}

data class ChartsState(
    val playlists: List<SquareDisplayItem> = emptyList(),
    val liked: List<SquareDisplayItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)