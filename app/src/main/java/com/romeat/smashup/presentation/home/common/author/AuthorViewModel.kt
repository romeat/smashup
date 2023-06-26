package com.romeat.smashup.presentation.home.common.author

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.author.GetAuthorUseCase
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.likes.LikesRepository
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
class AuthorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAuthorUseCase: GetAuthorUseCase,
    private val getMashupListUseCase: GetMashupsListUseCase,
    private val musicServiceConnection: MusicServiceConnection,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthorScreenState())
    val state = _state.asStateFlow()

    private val authorAlias: String =
        checkNotNull(savedStateHandle[CommonNavigationConstants.AUTHOR_PARAM])

    private var originalMashupList: List<Mashup> = emptyList()

    init {
        viewModelScope.launch {
            getAuthorUseCase
                .invoke(authorAlias)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { profile ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        authorInfo = profile,
                                        errorMessage = "",
                                        
                                        //todo restore when ready
                                        //isMashupListLoading = profile.mashups.isNotEmpty()
                                    )
                                }

                                /* todo restore when ready
                                if (profile.mashups.isNotEmpty()) {
                                    getMashups(profile.mashups)
                                }
                                 */
                            }
                        }
                        is Resource.Loading -> {
                            _state.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
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
        likesRepository
            .likesState
            .combine(getMashupListUseCase.invoke(ids)) { likes, mashups ->
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
            originalMashupList
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

data class AuthorScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val authorInfo: AuthorProfile? = null,

    val isMashupListLoading: Boolean = true,
    val isMashupListError: Boolean = false,
    val currentlyPlayingMashupId: Int? = null,
    val mashupList: List<MashupListItem> = emptyList()
)