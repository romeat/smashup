package com.romeat.smashup.presentation.home.common.playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getMashupListUseCase: GetMashupsListUseCase,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    //var state by mutableStateOf(PlaylistScreenState())

    private val _state = MutableStateFlow(PlaylistScreenState())
    val state = _state.asStateFlow()

    private val playlistId: Int = checkNotNull(savedStateHandle[CommonNavigationConstants.PLAYLIST_PARAM])

    init {
        viewModelScope.launch {
            getPlaylistUseCase
                .invoke(listOf(playlistId))
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.first()?.let { playlist ->
                                _state.update { it ->
                                    it.copy(
                                        isLoading = false,
                                        playlistInfo = playlist,
                                        errorMessage = ""
                                    )
                                }
                                getMashups(playlist.mashups)
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it ->
                                it.copy(isLoading = true)
                            }
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
        viewModelScope.launch {
            musicServiceConnection.nowPlayingMashup
                .collect { mashup ->
                    _state.update { it ->
                        it.copy(currentlyPlayingMashupId = mashup?.id)
                    }
                }
        }
    }

    private suspend fun getMashups(ids: List<Int>) {
        getMashupListUseCase
            .invoke(ids)
            .collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update { it ->
                            it.copy(
                                mashupList = result.data!!,
                                isMashupListLoading = false,
                                isMashupListError = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it ->
                            it.copy(
                                isMashupListLoading = true,
                                isMashupListError = false,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update { it ->
                            it.copy(
                                isMashupListLoading = false,
                                isMashupListError = true
                            )
                        }
                    }
                }
            }
    }

    fun onMashupClick(mashup: Mashup) {
        musicServiceConnection.playMashupFromPlaylist(mashup, state.value.mashupList)
    }
}

data class PlaylistScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val playlistInfo: Playlist? = null,

    val isMashupListLoading: Boolean = true,
    val isMashupListError: Boolean = false,
    val currentlyPlayingMashupId: Int? = null,
    val mashupList: List<Mashup> = emptyList()
)