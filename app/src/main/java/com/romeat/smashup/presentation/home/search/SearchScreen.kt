package com.romeat.smashup.presentation.home.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.presentation.home.common.composables.compact.MashupListCompact
import com.romeat.smashup.presentation.home.common.composables.compact.PlaylistRow
import com.romeat.smashup.presentation.home.common.composables.compact.SourceListCompact
import com.romeat.smashup.presentation.home.common.composables.compact.UserListCompact
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.presentation.home.common.composables.listitem.PlaylistItem
import com.romeat.smashup.presentation.home.common.composables.listitem.SourceItem
import com.romeat.smashup.presentation.home.common.composables.listitem.UserItem
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.compose.BackPressHandler


@Composable
fun SearchScreen(
    onMashupInfoClick: (Int) -> Unit,
    onSourceClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchBarViewModel = hiltViewModel()
) {
    val state by viewModel.resultState.collectAsState()
    val searchQueryState by viewModel.searchQueryState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        SearchScreenContent(
            state = state,
            searchQuery = searchQueryState,
            onQueryChange = viewModel::onQueryChange,
            clearInput = viewModel::clearInput,
            onMashupInfoClick = onMashupInfoClick,
            onMashupClick = viewModel::onMashupClick,
            onLikeClick = viewModel::onLikeClick,
            onSourceClick = onSourceClick,
            onUserClick = onUserClick,
            onPlaylistClick = onPlaylistClick,
            onBackClick = onBackClick,
        )
    }
}

@Composable
fun SearchScreenContent(
    state: SearchResultState,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    clearInput: () -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onMashupClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onSourceClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onBackClick: ()-> Unit,
) {
    val focusManager = LocalFocusManager.current
    val mashupListOpened = remember { mutableStateOf(false) }
    val userListOpened = remember { mutableStateOf(false) }
    val playlistListOpened = remember { mutableStateOf(false) }
    val sourceListOpened = remember { mutableStateOf(false) }

    BackPressHandler(
        onBackPressed = {
            val opened = listOf(mashupListOpened, userListOpened, playlistListOpened, sourceListOpened)
            if (opened.any { it.value == true }) {
                opened.firstOrNull { it.value == true }?.value = false
            } else {
                onBackClick()
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // All mashups
        if (mashupListOpened.value) {
            MashupFullListOverlay(
                mashupListOpened = mashupListOpened,
                state = state,
                onMashupClick = onMashupClick,
                onMashupInfoClick = onMashupInfoClick,
                onLikeClick = onLikeClick
            )
        }
        // All users
        if (userListOpened.value) {
            UserFullListOverlay(
                userListOpened = userListOpened,
                state = state,
                onClick = onUserClick
            )
        }
        // All playlists
        if (playlistListOpened.value) {
            PlaylistFullListOverlay(
                playlistListOpened = playlistListOpened,
                state = state,
                onClick = onPlaylistClick
            )
        }
        // All sources
        if (sourceListOpened.value) {
            SourceFullListOverlay(
                sourceListOpened = sourceListOpened,
                state = state,
                onClick = onSourceClick
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopRow(
                title = stringResource(R.string.search),
                onBackPressed = { },
                showBackButton = false
            )
            StyledInput(
                text = searchQuery,
                onTextChange = onQueryChange,
                placeholderResId = R.string.search_hint,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    if (searchQuery.isNotEmpty())
                        ClearInputTrailingIcon(onClick = clearInput)
                },
                enabled = true,
                isError = false,
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else if (state.isError) {
                ErrorTextMessage()
            } else if (state.isResultEmpty){
                ErrorTextMessage(R.string.search_nothing_found)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (state.mashups.isNotEmpty()) {
                        item {
                            MashupListCompact(
                                mashups = state.mashups,
                                onMashupClick = onMashupClick,
                                onLikeClick = onLikeClick,
                                onMashupInfoClick = onMashupInfoClick,
                                currentlyPlayingMashupId = state.currentlyPlayingMashupId,
                                onMoreClick = { mashupListOpened.value = true }
                            )
                        }
                    }
                    if (state.users.isNotEmpty()) {
                        item {
                            UserListCompact(
                                users = state.users,
                                onClick = onUserClick,
                                onMoreClick = { userListOpened.value = true }
                            )
                        }
                    }
                    if (state.playlists.isNotEmpty()) {
                        item {
                            PlaylistRow(
                                playlists = state.playlists,
                                onClick = onPlaylistClick,
                                onMoreClick = { playlistListOpened.value = true }
                            )
                        }
                    }
                    if (state.sources.isNotEmpty()) {
                        item {
                            SourceListCompact(
                                sources = state.sources,
                                onClick = onSourceClick,
                                onMoreClick = { sourceListOpened.value = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MashupFullListOverlay(
    mashupListOpened: MutableState<Boolean>,
    state: SearchResultState,
    onMashupClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(7f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopRow(title = stringResource(id = R.string.all_mashups), onBackPressed = { mashupListOpened.value = false })
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(
                    items = state.mashups,
                    key = { it.id }
                ) { mashup ->
                    MashupItem(
                        mashup = mashup,
                        onBodyClick = { onMashupClick(it.id) },
                        onInfoClick = onMashupInfoClick,
                        onLikeClick = onLikeClick,
                        isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                            ?: false
                    )
                }
            }
        }
    }
}

@Composable
fun UserFullListOverlay(
    userListOpened: MutableState<Boolean>,
    state: SearchResultState,
    onClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(6f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopRow(title = stringResource(id = R.string.all_users), onBackPressed = { userListOpened.value = false })
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(vertical = 15.dp),
            ) {
                items(
                    items = state.users,
                    key = { it.id }
                ) { user ->
                    UserItem(user = user, onClick = { onClick(user.id) })
                }
            }
        }
    }
}

@Composable
fun PlaylistFullListOverlay(
    playlistListOpened: MutableState<Boolean>,
    state: SearchResultState,
    onClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(5f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopRow(title = stringResource(id = R.string.all_playlists), onBackPressed = { playlistListOpened.value = false })
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(
                    items = state.playlists,
                    key = { it.id }
                ) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        onClick = { onClick(playlist.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun SourceFullListOverlay(
    sourceListOpened: MutableState<Boolean>,
    state: SearchResultState,
    onClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(4f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopRow(title = stringResource(id = R.string.all_sources), onBackPressed = { sourceListOpened.value = false })
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(
                    items = state.sources,
                    key = { it.id }
                ) { source ->
                    SourceItem(source = source, onClick = { onClick(source.id) })
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

@Preview
@Composable
fun SearchScreenContentPreview(
    @PreviewParameter(SearchScreenStateProvider::class) state: SearchResultState
) {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            SearchScreenContent(
                state = state,
                searchQuery = "Moviestra",
                {}, {}, {}, {}, {}, {}, {}, {}, {}
            )
        }
    }
}

class SearchScreenStateProvider : PreviewParameterProvider<SearchResultState> {
    override val values = listOf(
        SearchResultState(),
        SearchResultState(isLoading = true),
        SearchResultState(isResultEmpty = true),
        SearchResultState(isError = true),
        SearchResultState(
            mashups = listOf(
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(15, "popandos", "lavandos", "null", false, 1, 1, emptyList(), false),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
            )
        ),
        SearchResultState(
            mashups = listOf(
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
            ),
            playlists = listOf(
                Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                Playlist(15, "Yooooo", "", listOf("BRUH"), "def", listOf(),1,1),
                Playlist(35, "Playlist dlya mochalki", "", listOf("KEKW"), "def", listOf(),1,1),
            )
        ),
        SearchResultState(
            mashups = listOf(
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
            ),
            playlists = listOf(
                Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                Playlist(15, "Yooooo", "", listOf("BRUH"), "def", listOf(),1,1),
                Playlist(35, "Playlist dlya mochalki", "", listOf("KEKW"), "def", listOf(),1,1),
            )
        )
    ).asSequence()
}
