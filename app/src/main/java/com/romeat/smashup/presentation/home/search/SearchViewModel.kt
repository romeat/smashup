package com.romeat.smashup.presentation.home.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.domain.search.*
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor(
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase,
    private val searchMashupsUseCase: SearchMashupsUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchSourcesUseCase: SearchSourcesUseCase,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val searchQueryMinSymbols = 4
    private val searchQueryMaxSymbols = 32

    var resultState by mutableStateOf(SearchResultState())

    var currentlyPlaying: Int? by mutableStateOf(null)

    private val _searchQueryState = MutableStateFlow(SearchQueryState())
    val searchQueryState = _searchQueryState.asStateFlow()

    private var activeSearchJob: Job? = null

    init {
        viewModelScope.launch {
            searchQueryState
                .debounce(500L)
                .collectLatest { value ->
                    if (value.query.length < searchQueryMinSymbols) {
                        resultState = resultState.copy(
                            isLoading = false,
                            result = null
                        )
                    } else if (value.query.length in (searchQueryMinSymbols..searchQueryMaxSymbols)) {
                        performSearch()
                    }
                }
        }
        viewModelScope.launch {
            musicServiceConnection.nowPlayingMashup
                .collect { mashup ->
                    currentlyPlaying = mashup?.id
                }
        }
    }

    fun onQueryChange(query: String) {
        if (query.length <= searchQueryMaxSymbols) {
            _searchQueryState.value = _searchQueryState.value.copy(query = query)
        }
    }

    fun clearInput() {
        _searchQueryState.value = _searchQueryState.value.copy(
            query = ""
        )
    }

    fun onSearchOptionClick(option: SearchOption) {
        if (_searchQueryState.value.searchBy != option) {
            _searchQueryState.value = _searchQueryState.value.copy(searchBy = option)
        }
    }

    fun onMashupClick(mashup: Mashup) {
        musicServiceConnection.playMashupFromPlaylist(mashup, (resultState.result as SearchResult.Mashups).list)
    }

    private fun performSearch() {
        activeSearchJob?.cancel()
        activeSearchJob = viewModelScope.launch {
            val useCase: SearchUseCase = when (searchQueryState.value.searchBy) {
                is SearchOption.ByMashups -> searchMashupsUseCase
                is SearchOption.ByPlaylist -> searchPlaylistsUseCase
                is SearchOption.BySource -> searchSourcesUseCase
                is SearchOption.ByAuthor -> searchAuthorsUseCase
            }
            invokeUseCase(useCase)
        }
    }

    private suspend fun invokeUseCase(useCase: SearchUseCase) {
        useCase
            .invoke(searchQueryState.value.query)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            resultState = resultState.copy(
                                result = result.data,
                                isLoading = false,
                                isResultEmpty = it.size == 0
                            )
                        }
                    }
                    is Resource.Loading -> {
                        if (!resultState.isLoading) {
                            resultState = resultState.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        resultState = resultState.copy(
                            isLoading = false,
                            isError = true,
                            result = null,
                        )
                    }
                }
            }
    }
}

data class SearchQueryState(
    val query: String = "",
    val searchBy: SearchOption = SearchOption.ByMashups
)

data class SearchResultState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isResultEmpty: Boolean = false,
    val result: SearchResult? = null
)

sealed class SearchResult() {
    abstract val size: Int

    data class Mashups(
        val list: List<Mashup>,
        override val size: Int = list.size
    ) : SearchResult()

    data class Authors(
        val list: List<AuthorProfile>,
        override val size: Int = list.size
    ) : SearchResult()

    data class Playlists(
        val list: List<Playlist>,
        override val size: Int = list.size
    ) : SearchResult()

    data class Sources(
        val list: List<Source>,
        override val size: Int = list.size
    ) : SearchResult()
}