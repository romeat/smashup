package com.romeat.smashup.presentation.home.common.playlist.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPlaylistsUseCase: GetPlaylistUseCase,
) : ViewModel() {

    private val playlistIds: ArrayList<Int> =
        checkNotNull(savedStateHandle[CommonNavigationConstants.PLAYLIST_LIST_PARAM])

    private val _state = MutableStateFlow(PlaylistListScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getPlaylistsUseCase
                .invoke(playlistIds)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { playlists ->
                                _state.update { it ->
                                    it.copy(
                                        isLoading = false,
                                        playlists = playlists,
                                    )
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Error -> {
                            _state.update { it ->
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message!!
                                )
                            }
                        }
                    }
                }
        }
    }
}

data class PlaylistListScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val playlists: List<Playlist> = emptyList(),
)