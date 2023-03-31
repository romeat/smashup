package com.romeat.smashup.presentation.home.common.author

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.author.GetAuthorUseCase
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.data.dto.Mashup
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
class AuthorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAuthorUseCase: GetAuthorUseCase,
    private val getMashupListUseCase: GetMashupsListUseCase,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    //var state by mutableStateOf(AuthorScreenState())

    private val _state = MutableStateFlow(AuthorScreenState())
    val state = _state.asStateFlow()

    private val authorAlias: String =
        checkNotNull(savedStateHandle[CommonNavigationConstants.AUTHOR_PARAM])

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
                                        isMashupListLoading = profile.mashups.isNotEmpty()
                                    )
                                }

                                if (profile.mashups.isNotEmpty()) {
                                    getMashups(profile.mashups)
                                }
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
        getMashupListUseCase
            .invoke(ids)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                mashupList = result.data!!,
                                isMashupListLoading = false,
                                isMashupListError = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isMashupListLoading = true,
                                isMashupListError = false,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
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

data class AuthorScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val authorInfo: AuthorProfile? = null,

    val isMashupListLoading: Boolean = true,
    val isMashupListError: Boolean = false,
    val currentlyPlayingMashupId: Int? = null,
    val mashupList: List<Mashup> = emptyList()
)