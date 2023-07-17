package com.romeat.smashup.presentation.home.common.mashup

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.mashups.GetMashupUseCase
import com.romeat.smashup.domain.mashups.GetMashupsListUseCase
import com.romeat.smashup.domain.mashups.GetSourcesListUseCase
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMashupUseCase: GetMashupUseCase,
    private val getSourcesListUseCase: GetSourcesListUseCase
) : ViewModel() {

    var state by mutableStateOf(MashupScreenState())

    private val mashupId: Int = checkNotNull(savedStateHandle[CommonNavigationConstants.MASHUP_PARAM])

    init {
        viewModelScope.launch {
            getMashupUseCase
                .invoke(mashupId)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.first()?.let {
                                state = state.copy(
                                    isLoading = false,
                                    mashupInfo = it,
                                    errorMessage = ""
                                )
                                getSources(it.tracks)
                            }
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                errorMessage = result.message!!
                            )
                        }
                    }
                }
        }
    }

    private suspend fun getSources(ids: List<Int>) {
        getSourcesListUseCase
            .invoke(ids)
            .collect { result ->
                when(result) {
                    is Resource.Success -> {
                        state = state.copy(
                            sourceList = result.data!!,
                            isSourceListLoading = false,
                            isSourceListError = false,
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            isSourceListLoading = true,
                            isSourceListError = false,
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            isSourceListLoading = false,
                            isSourceListError = true
                        )
                    }
                }
            }
    }
}

@Stable
data class MashupScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",

    val mashupInfo: Mashup? = null,

    val isSourceListLoading: Boolean = true,
    val isSourceListError: Boolean = false,
    val sourceList: List<Source> = emptyList()
)