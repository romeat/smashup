package com.romeat.smashup.presentation.home.search

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.dto.*
import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.domain.search.*
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.ConvertToUiListItems
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchBarViewModel @Inject constructor(
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase,
    private val searchMashupsUseCase: SearchMashupsUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchSourcesUseCase: SearchSourcesUseCase,
    private val musicServiceConnection: MusicServiceConnection,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val searchQueryMinSymbols = 4
    private val searchQueryMaxSymbols = 32

    private val _resultState = MutableStateFlow(SearchResultState())
    val resultState = _resultState.asStateFlow()

    private val _searchQueryState = MutableStateFlow("")
    val searchQueryState = _searchQueryState.asStateFlow()

    private var activeSearchJob: Job? = null

    init {
        viewModelScope.launch {
            searchQueryState
                .debounce(500L)
                .collectLatest { value ->
                    if (value.length < searchQueryMinSymbols) {
                        _resultState.update {
                            it.copy(
                                isLoading = false,
                                isResultEmpty = false,
                                isError = false,
                            )
                        }
                        clearResults()
                    } else if (value.length in (searchQueryMinSymbols..searchQueryMaxSymbols)) {
                        clearResults()
                        performSearch()
                    }
                }
        }
        viewModelScope.launch {
            musicServiceConnection.nowPlayingMashup
                .collect { mashup ->
                    _resultState.update { it.copy(currentlyPlayingMashupId = mashup?.id) }
                }
        }
        viewModelScope.launch {
            likesRepository.likesState
                .collect{ likes ->
                    _resultState.update {
                        it.copy(
                            mashups = ConvertToUiListItems(
                                _resultState.value.originalMashups,
                                likes.mashupLikes
                            )
                        )
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        if (query.length <= searchQueryMaxSymbols) {
            _searchQueryState.value = query
        }
    }

    fun clearInput() {
        _searchQueryState.value = ""
    }

    fun onLikeClick(mashupId: Int) {
        if (likesRepository.likesState.value.mashupLikes.contains(mashupId)) {
            likesRepository.removeLike(mashupId)
        } else {
            likesRepository.addLike(mashupId)
        }
    }

    fun onMashupClick(mashupId: Int) {
        musicServiceConnection.playMashupFromPlaylist(
            mashupId,
            resultState.value.originalMashups
        )
    }

    private fun performSearch() {
        activeSearchJob?.cancel()
        activeSearchJob = viewModelScope.launch {
            combine(
                searchMashupsUseCase(searchQueryState.value),
                searchAuthorsUseCase(searchQueryState.value),
                searchPlaylistsUseCase(searchQueryState.value),
                searchSourcesUseCase(searchQueryState.value),
            ) { mashups, authors, playlists, sources ->
                listOf(mashups, authors, playlists, sources)
            }.collect { results ->
                if (results.any { it is Resource.Loading }) {
                    _resultState.update { it.copy(isLoading = true) }
                } else if (results.all { it is Resource.Error }) {
                    _resultState.update {
                        it.copy(isLoading = false, isError = true)
                    }
                } else { // no loading states, and at least one success state
                    _resultState.update { it.copy(isLoading = false) }

                    if (results.filter { it is Resource.Success }.all { it.data!!.isEmpty() }) {
                        // nothing found
                        _resultState.update { it.copy(isResultEmpty = true) }
                    } else {
                        _resultState.update {
                            it.copy(
                                originalMashups = results[0].data!! as? List<Mashup> ?: emptyList(),
                                mashups = ConvertToUiListItems(
                                    results[0].data!! as? List<Mashup> ?: emptyList(),
                                    likesRepository.likesState.value.mashupLikes
                                ),
                                users = results[1].data!! as? List<UserProfile> ?: emptyList(),
                                playlists = results[2].data!! as? List<Playlist> ?: emptyList(),
                                sources = results[3].data!! as? List<Source> ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun clearResults() {
        _resultState.update {
            it.copy(
                originalMashups = emptyList(),
                mashups = emptyList(),
                users = emptyList(),
                playlists = emptyList(),
                sources = emptyList()
            )
        }
    }
}

@Stable
data class SearchResultState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isResultEmpty: Boolean = false,

    val originalMashups: List<Mashup> = emptyList(),
    val currentlyPlayingMashupId: Int? = null,

    val mashups: List<MashupListItem> = emptyList(),
    val users: List<UserProfile> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val sources: List<Source> = emptyList(),
)