package com.romeat.smashup.presentation.home.common.source

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.domain.mashups.GetMashupsWithSourceUseCase
import com.romeat.smashup.domain.mashups.GetSourceUseCase
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
class SourceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSourceUseCase: GetSourceUseCase,
    private val getMashupsWithSourceUseCase: GetMashupsWithSourceUseCase,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    //var state by mutableStateOf(SourceScreenState())

    private val _state = MutableStateFlow(SourceScreenState())
    val state = _state.asStateFlow()

    private val sourceId: Int =
        checkNotNull(savedStateHandle[CommonNavigationConstants.SOURCE_PARAM])

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
        getMashupsWithSourceUseCase
            .invoke(id)
            .collect { result ->
                when (result) {
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

data class SourceScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val sourceInfo: Source? = null,

    val isMashupListLoading: Boolean = true,
    val isMashupListError: Boolean = false,
    val currentlyPlayingMashupId: Int? = null,
    val mashupList: List<Mashup> = emptyList()
)