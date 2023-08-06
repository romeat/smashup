package com.romeat.smashup.presentation.home.common.mashup

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.mashups.GetSourcesListUseCase
import com.romeat.smashup.domain.user.GetUserListUseCase
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSourcesListUseCase: GetSourcesListUseCase,
    private val getAuthorsUseCase: GetUserListUseCase,
    private val likesRepository: LikesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MashupScreenState(
        Json.decodeFromString<Mashup>(checkNotNull(savedStateHandle[CommonNavigationConstants.MASHUP_SERIALIZED]))
    ))
    val state = _state.asStateFlow()

    //    private val mashupId: Int =
//        checkNotNull(savedStateHandle[CommonNavigationConstants.MASHUP_PARAM])
    private val mashupJson: String =
        checkNotNull(savedStateHandle[CommonNavigationConstants.MASHUP_SERIALIZED])

    init {
//        val mashup = Json.decodeFromString<Mashup>(mashupJson)
//
//        _state.update { it.copy(mashupInfo = mashup) }

        viewModelScope.launch {
            likesRepository.likesState.collect { likesState ->
                _state.update {
                    it.copy(isLiked = likesState.mashupLikes.contains(it.mashupInfo.id))
                }
            }
        }
        viewModelScope.launch {
            getSources(state.value.mashupInfo.tracks)
            getAuthors(state.value.mashupInfo.authorsIds)
        }
    }

    private suspend fun getSources(ids: List<Int>) {
        getSourcesListUseCase
            .invoke(ids)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                sourceList = result.data!!,
                                isSourceListLoading = false,
                                isSourceListError = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isSourceListLoading = true,
                                isSourceListError = false,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isSourceListLoading = false,
                                isSourceListError = true
                            )
                        }
                    }
                }
            }
    }

    private suspend fun getAuthors(ids: List<Int>) {
        getAuthorsUseCase
            .invoke(ids)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                authorList = result.data!!,
                                isAuthorListLoading = false,
                                isAuthorListError = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isAuthorListLoading = true,
                                isAuthorListError = false,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isAuthorListLoading = false,
                                isAuthorListError = true
                            )
                        }
                    }
                }
            }
    }

    fun onLikeClick() {
        if (state.value.isLiked) {
            likesRepository.removeLike(state.value.mashupInfo.id)
        } else {
            likesRepository.addLike(state.value.mashupInfo.id)
        }
    }
}

@Stable
data class MashupScreenState(

    val mashupInfo: Mashup,
    val isLiked: Boolean = false,

    val isSourceListLoading: Boolean = true,
    val isSourceListError: Boolean = false,
    val sourceList: List<Source> = emptyList(),

    val isAuthorListLoading: Boolean = true,
    val isAuthorListError: Boolean = false,
    val authorList: List<UserProfile> = emptyList(),
) {
    val isProgress
        get() = isAuthorListLoading || isSourceListLoading
}