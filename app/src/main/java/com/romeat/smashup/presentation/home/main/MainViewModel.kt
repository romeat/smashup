package com.romeat.smashup.presentation.home.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.playlists.GetCompilationsUseCase
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val compilationsUseCase: GetCompilationsUseCase
) : ViewModel() {

    var state by mutableStateOf(ChartsState())

    init {
        viewModelScope.launch {
            compilationsUseCase
                .invoke()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(
                                    playlists = result.data,//.sortedByDescending { it.id },
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
                                errorMessage = result.message!!
                            )
                        }
                    }
                }
        }
    }

    fun onPlaylistSelect(id: Int) {

    }
}

data class ChartsState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)