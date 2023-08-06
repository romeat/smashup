package com.romeat.smashup.presentation.home.main

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.domain.recommendations.GetRecommendationsUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.presentation.home.MusicServiceViewModel
import com.romeat.smashup.presentation.home.PlaylistTitle
import com.romeat.smashup.util.ConvertToUiListItems
import com.romeat.smashup.util.PlaylistsToSquareDisplayItems
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SquareDisplayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getMashupListUseCase: GetMashupsListUseCase,
    private val likesRepository: LikesRepository,
    musicServiceConnection: MusicServiceConnection
) : MusicServiceViewModel(musicServiceConnection) {

    private val _state = MutableStateFlow(ChartsState())
    val state = _state.asStateFlow()

    init {
        playlistTitle = PlaylistTitle.ResType(R.string.playlist_recommendations)

        viewModelScope.launch {
            getCompilations()
            getRecommendationsIds()
        }
        viewModelScope.launch {
            musicServiceConnection.nowPlayingMashup
                .debounce(100)
                .collect { mashup ->
                    _state.update { it ->
                        it.copy(currentlyPlayingMashupId = mashup?.id)
                    }
                }
        }
    }

    private suspend fun getCompilations() {
        getPlaylistUseCase
            .invoke(listOf(1, 2, 3))
            .collect { result ->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.update {
                                it.copy(
                                    playlists = PlaylistsToSquareDisplayItems(result.data),
                                    isLoading = false,
                                    isError = false
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _state.update{ it.copy(isLoading = true) }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = result.message ?: "some error"
                            )
                        }
                    }
                }
            }
    }

    private suspend fun getRecommendationsIds() {
        getRecommendationsUseCase
            .invoke()
            .collect { result ->
                when(result) {
                    is Resource.Success -> {
                        getMashups(result.data!!)
                    }
                    is Resource.Loading -> {
                        _state.update{ it.copy(recommendationsLoaded = false) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(recommendationsLoaded = true) }
                    }
                }
            }
    }

    private suspend fun getMashups(ids: List<Int>) {
        if (ids.isEmpty()) {
            _state.update {
                it.copy(
                    recommendationsLoaded = true,
                    recommendations = emptyList()
                )
            }
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
                        _state.update { it ->
                            it.copy(
                                recommendations = ConvertToUiListItems(
                                    pair.second.data!!,
                                    pair.first.mashupLikes
                                ),
                                recommendationsLoaded = true,
                            )
                        }
                        originalMashupList = pair.second.data!!
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(recommendationsLoaded = false) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(recommendationsLoaded = true) }
                    }
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
}

@Stable
data class ChartsState(
    val isLoading: Boolean = false,

    val playlists: List<SquareDisplayItem> = emptyList(),

    val recommendations: List<MashupListItem> = emptyList(),
    val recommendationsLoaded: Boolean = false,

    val isError: Boolean = false,
    val errorMessage: String = "",

    val currentlyPlayingMashupId: Int? = null,
) {
    val isProgress
        get() = isLoading || !recommendationsLoaded
}