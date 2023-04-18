package com.romeat.smashup.presentation.home.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.toMashupUListItem
import com.romeat.smashup.presentation.home.common.composables.*

@Composable
fun SearchScreen(
    onMashupInfoClick: (Int) -> Unit,
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    viewModel: SearchBarViewModel = hiltViewModel()
) {
    val state = viewModel.resultState
    val searchQueryState by viewModel.searchQueryState.collectAsState()
    val currentlyPlayingMashupId = viewModel.currentlyPlaying

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        Text(
            text = stringResource(id = R.string.search),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h4.fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        )
        OutlinedTextField(
            value = searchQueryState.query,
            onValueChange = { newValue -> viewModel.onQueryChange(newValue) },
            placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
            maxLines = 1,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            trailingIcon = {
                if (searchQueryState.query.isNotEmpty())
                    ClearInputTrailingIcon(onClick = { viewModel.clearInput() })
            }

        )
        SearchOptionsButtonRow(
            searchQueryState = searchQueryState,
            onButtonClick = { option -> viewModel.onSearchOptionClick(option) },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else if (state.isError) {
                Text(
                    text = stringResource(id = R.string.error_during_search),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    textAlign = TextAlign.Center
                )
            } else if (state.isResultEmpty) {
                Text(
                    text = stringResource(id = R.string.no_results),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                when (state.result) {
                    is SearchResult.Authors -> {
                        AuthorsGrid(
                            authors = state.result.list,
                            onAuthorClick = { name -> onAuthorClick(name) }
                        )
                    }
                    is SearchResult.Mashups -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.result.list.size) { i ->
                                val mashup = state.result.list[i].toMashupUListItem()
                                MashupItem(
                                    mashup = mashup,
                                    onBodyClick = { viewModel.onMashupClick(it.id) },
                                    onInfoClick = { id -> onMashupInfoClick(id) },
                                    isCurrentlyPlaying = currentlyPlayingMashupId == mashup.id,
                                    onLikeClick = { }
                                )
                                if (i < state.result.list.size) {
                                    Divider(
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    is SearchResult.Sources -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.result.list.size) { i ->
                                val source = state.result.list[i]
                                SourceItem(
                                    source = state.result.list[i],
                                    onClick = { id -> onSourceClick(id) }
                                )
                                if (i < state.result.list.size) {
                                    Divider(
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    is SearchResult.Playlists -> {
                        PlaylistsGrid(
                            playlists = state.result.list,
                            onPlaylistClick = { id -> onPlaylistClick(id) }
                        )
                    }
                    null -> {}
                }
            }
        }
    }
}

@Composable
fun ClearInputTrailingIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "clear field")
    }
}

@Composable
fun SearchOptionsButtonRow(
    searchQueryState: SearchQueryState,
    onButtonClick: (SearchOption) -> Unit
) {
    val searchOptions = listOf(
        SearchOption.ByMashups,
        SearchOption.ByAuthor,
        SearchOption.BySource,
        SearchOption.ByPlaylist
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        items(searchOptions.size) { index ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(10.dp))
            }
            val backgroundColor =
                if (searchQueryState.searchBy == searchOptions[index])
                    MaterialTheme.colors.primaryVariant
                else
                    Color.Transparent

            val textColor =
                if (searchQueryState.searchBy == searchOptions[index])
                    MaterialTheme.colors.background
                else
                    MaterialTheme.colors.primaryVariant
            OutlinedButton(
                onClick = { onButtonClick(searchOptions[index]) },
                border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
                shape = RoundedCornerShape(50), // = 50% percent
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = backgroundColor,
                    contentColor = textColor
                ),
            ) {
                Text(
                    text = stringResource(id = searchOptions[index].stringRes),
                    fontSize = MaterialTheme.typography.button.fontSize
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun SuccessfulSearchResult(result: SearchResult?) {

}


sealed class SearchOption(val stringRes: Int) {
    object ByMashups : SearchOption(R.string.search_mashups)
    object ByAuthor : SearchOption(R.string.search_authors)
    object BySource : SearchOption(R.string.search_sources)
    object ByPlaylist : SearchOption(R.string.search_playlists)
}
