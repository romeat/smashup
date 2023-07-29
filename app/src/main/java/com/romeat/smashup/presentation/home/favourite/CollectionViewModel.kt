package com.romeat.smashup.presentation.home.favourite

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.domain.user.GetUserUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.presentation.home.MusicServiceViewModel
import com.romeat.smashup.util.ConvertToUiListItems
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val likesRepository: LikesRepository,
    private val userRepository: LoggedUserRepository,
    private val getUserUseCase: GetUserUseCase,
    private val getMashupsListUseCase: GetMashupsListUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    musicServiceConnection: MusicServiceConnection,
) : MusicServiceViewModel(musicServiceConnection) {

    private val _state = MutableStateFlow(CollectionState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            likesRepository.updateLikesManually()
            delay(300)
            getMyPlaylists()
            getLikedMashups()
        }

        viewModelScope.launch {
            musicServiceConnection.nowPlayingMashup
                .collect { mashup ->
                    _state.update { it.copy(currentlyPlayingMashupId = mashup?.id) }
                }
        }
    }

    fun onLikeClick(mashupId: Int) {
        if (likesRepository.likesState.value.mashupLikes.contains(mashupId)) {
            likesRepository.removeLike(mashupId)
        } else {
            likesRepository.addLike(mashupId)
        }
    }

    private suspend fun getMyPlaylists() {
        getUserUseCase(userRepository.userInfoFlow.value!!.id)
            .collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, isError = true) }
                    }
                    is Resource.Success -> {
                        val user = result.data!!
                        if (user.playlists.isEmpty()) {
                            _state.update { it.copy(isLoading = false, playlistsLoaded = true) }
                        } else {
                            getActualPlaylists(user.playlists)
                        }
                    }
                }
            }
    }

    private suspend fun getActualPlaylists(ids: List<Int>) {
        getPlaylistUseCase(ids)
            .collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, isError = true) }
                    }
                    is Resource.Success -> {
                        val playlists = result.data!!
                        _state.update {
                            it.copy(
                                isLoading = false,
                                playlistsLoaded = true,
                                myPlaylists = playlists,
                            )
                        }
                    }
                }
            }
    }

    private suspend fun getLikedMashups() {
        likesRepository.likesState.collect { state ->
            if (state.mashupLikes.isEmpty()) {
                _state.update { it.copy(isLoading = false, mashupsLoaded = true) }
                return@collect
            }
            getMashupsListUseCase(state.mashupLikes.toList())
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false, isError = true) }
                        }
                        is Resource.Success -> {
                            val mashups = result.data!!
                            originalMashupList = mashups
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    mashupsLoaded = true,
                                    myLikedMashups = ConvertToUiListItems(
                                        mashups,
                                        likesRepository.likesState.value.mashupLikes
                                    ),
                                )
                            }
                        }
                    }
                }
        }
    }
}

@Stable
data class CollectionState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,

    val currentlyPlayingMashupId: Int? = null,

    val myLikedMashups: List<MashupListItem> = emptyList(),
    val mashupsLoaded: Boolean = false,
    val myPlaylists: List<Playlist> = emptyList(),
    val playlistsLoaded: Boolean = false,
) {
    val isEmpty: Boolean
        get() = mashupsLoaded && playlistsLoaded && myLikedMashups.isEmpty() && myPlaylists.isEmpty()
}