package com.romeat.smashup.presentation.home.common.user

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.user.GetUserUseCase
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.ConvertToUiListItems
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAuthorUseCase: GetUserUseCase,
    private val getMashupListUseCase: GetMashupsListUseCase,
    private val getPlaylistsUseCase: GetPlaylistUseCase,
    private val musicServiceConnection: MusicServiceConnection,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserScreenState())
    val state = _state.asStateFlow()

    private val authorId: Int =
        checkNotNull(savedStateHandle[CommonNavigationConstants.USER_PARAM])

    init {
        viewModelScope.launch {
            getAuthorUseCase
                .invoke(authorId)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            result.data?.let { profile ->
                                if (profile.mashups.isEmpty() && profile.playlists.isEmpty()) {
                                    _state.update { it.copy(isEmpty = true) }
                                    return@collect
                                }
                                if (profile.mashups.isNotEmpty()) {
                                    getMashups(profile.mashups)
                                }
                                if (profile.playlists.isNotEmpty()) {
                                    getPlaylists(profile.playlists)
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "error loading data"
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
        if (ids.isEmpty()) {
            _state.update { it.copy(mashupsLoaded = true) }
            return
        }
        likesRepository
            .likesState
            .combine(getMashupListUseCase.invoke(ids)) { likes, mashups ->
                Pair(likes, mashups)
            }
            .collect { pair ->
                when (pair.second) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                mashupsLoaded = true,
                                originalMashupList = pair.second.data!!,
                                mashupList = ConvertToUiListItems(
                                    pair.second.data!!,
                                    pair.first.mashupLikes
                                ),
                            )
                        }
                    }
                    is Resource.Loading -> { }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                mashupsLoaded = true,
                                errorMessage = "failed to get mashups"
                            )
                        }
                    }
                }
            }
    }

    private suspend fun getPlaylists(ids: List<Int>) {
        if (ids.isEmpty()) {
            _state.update { it.copy(playlistsLoaded = true) }
            return
        }
        getPlaylistsUseCase(ids)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update { it ->
                            it.copy(
                                playlistsLoaded = true,
                                playlistList = result.data!!,
                            )
                        }
                    }
                    is Resource.Loading -> { }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                playlistsLoaded = true,
                                errorMessage = "failed to get playlists"
                            )
                        }
                    }
                }
            }
    }

    fun onMashupClick(mashupId: Int) {
        musicServiceConnection.playMashupFromPlaylist(
            mashupId,
            _state.value.originalMashupList
        )
    }

    fun onLikeClick(mashupId: Int) {
        if (likesRepository.likesState.value.mashupLikes.contains(mashupId)) {
            likesRepository.removeLike(mashupId)
        } else {
            likesRepository.addLike(mashupId)
        }
    }
}

@Stable
data class UserScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val userInfo: UserProfile? = null,
    val isEmpty: Boolean = false,

    val currentlyPlayingMashupId: Int? = null,
    val originalMashupList: List<Mashup> = emptyList(),

    val mashupsLoaded: Boolean = false,
    val mashupList: List<MashupListItem> = emptyList(),
    val playlistsLoaded: Boolean = false,
    val playlistList: List<Playlist> = emptyList(),
)