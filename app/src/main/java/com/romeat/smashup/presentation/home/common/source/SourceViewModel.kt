package com.romeat.smashup.presentation.home.common.source

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.mashups.GetMashupsWithSourceUseCase
import com.romeat.smashup.domain.mashups.GetSourceUseCase
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
class SourceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSourceUseCase: GetSourceUseCase,
    private val getMashupsWithSourceUseCase: GetMashupsWithSourceUseCase,
    private val musicServiceConnection: MusicServiceConnection,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SourceScreenState())
    val state = _state.asStateFlow()

    private val sourceId: Int =
        checkNotNull(savedStateHandle[CommonNavigationConstants.SOURCE_PARAM])

    private var originalMashupList: List<Mashup> = emptyList()

    init {
        viewModelScope.launch {
            getSourceUseCase
                .invoke(sourceId)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.first()?.let { source ->
                                _state.update { it ->
                                    it.copy(
                                        isLoading = false,
                                        sourceInfo = source,
                                        errorMessage = ""
                                    )
                                }
                                getMashupsWithSource(sourceId)
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

    private suspend fun getMashupsWithSource(id: Int) {
        likesRepository
            .likesState
            .combine(getMashupsWithSourceUseCase.invoke(id)) { likes, mashups ->
                Pair(likes, mashups)
            }
            .collect { pair ->
                when (pair.second) {
                    is Resource.Success -> {
                        _state.update { it ->
                            originalMashupList = pair.second.data!!
                            it.copy(
                                mashupList = ConvertToUiListItems(
                                    pair.second.data!!,
                                    pair.first.mashupLikes
                                ),
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

    fun onMashupClick(mashupId: Int) {
        musicServiceConnection.playMashupFromPlaylist(
            mashupId,
            originalMashupList//ConvertFromUiListItems(state.value.mashupList)
        )
    }

    fun onLikeClick(mashupId: Int) {
        if (likesRepository.likesState.value.mashupLikes.contains(mashupId)) {
            likesRepository.removeLike(mashupId)
        } else {
            likesRepository.addLike(mashupId)
        }
    }

    fun onPlayClick() {
        playCurrentPlaylist(mashupIdToStart = state.value.mashupList.first().id)
    }

    fun onShuffleClick() {
        playCurrentPlaylist(mashupIdToStart = state.value.mashupList.first().id, shuffle = true)
    }

    private fun playCurrentPlaylist(mashupIdToStart: Int, shuffle: Boolean = false) {
        musicServiceConnection.playEntirePlaylist(
            mashupIdToStart = mashupIdToStart,
            playlist = originalMashupList,
            shuffle = shuffle
        )
    }
}

data class SourceScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val sourceInfo: Source? = null,

    val isMashupListLoading: Boolean = true,
    val isMashupListError: Boolean = false,
    val currentlyPlayingMashupId: Int? = null,
    val mashupList: List<MashupListItem> = emptyList()
)