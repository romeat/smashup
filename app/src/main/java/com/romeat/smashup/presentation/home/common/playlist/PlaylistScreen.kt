package com.romeat.smashup.presentation.home.common.playlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun PlaylistScreen(
    onMashupInfoClick: (String) -> Unit,
    onAuthorClick: (Int) -> Unit,
    onBackClicked: () -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        PlaylistScreenContent(
            onMashupInfoClick = onMashupInfoClick,
            //onAuthorClick = onAuthorClick,
            onBackClicked = onBackClicked,
            onLikeClick = viewModel::onLikeClick,
            onMashupClick = viewModel::onMashupClick,
            onShuffleClick = viewModel::onShuffleClick,
            onPlayClick = viewModel::onPlayClick,
            state = state
        )
    }
}

@Composable
fun PlaylistScreenContent(
    onMashupInfoClick: (String) -> Unit,
    //onAuthorClick: (Int) -> Unit,
    onBackClicked: () -> Unit,
    onLikeClick: (Int) -> Unit,
    onMashupClick: (Int) -> Unit,
    onShuffleClick: () -> Unit,
    onPlayClick: () -> Unit,
    state: PlaylistScreenState,
) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TransparentTopRow(
            onBackPressed = onBackClicked,
            modifier = Modifier.zIndex(2f),
            scrollState = scrollState
        )
        if (state.isProgress) {
            CustomCircularProgressIndicator()
        } else if (state.errorMessage.isNotBlank()) {
            ErrorTextMessage()
        } else {
            val info = state.playlistInfo!!
            LazyColumn(state = scrollState) {
                item {
                    PlayableHeader(
                        imageUrl = ImageUrlHelper.playlistImageIdToUrl400px(info.imageUrl),
                        title = info.name,
                        subtitle = info.owner,
                        mashupsCount = info.mashups.size,
                        onPlayPauseClick = onPlayClick,
                        onShuffleClick = onShuffleClick,
                        backgroundColor = info.backgroundColor
                    )
                }
                if (state.isMashupListLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            CustomCircularProgressIndicator()
                        }
                    }
                } else if (state.isMashupListError) {
                    item {
                        ErrorTextMessage()
                    }
                } else if (state.isMashupListEmpty) {
                    item {
                        ErrorTextMessage(R.string.playlist_empty)
                    }
                } else {
                    items(
                        items = state.mashupList,
                        key = { it.id }
                    ) { mashup ->
                        MashupItem(
                            mashup = mashup,
                            onBodyClick = { onMashupClick(it.id) },
                            onInfoClick = { onMashupInfoClick(mashup.serializedMashup) },
                            onLikeClick = { id -> onLikeClick(id)},
                            isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                                ?: false
                        )
                    }
                }
            }
        }
    }
}

@Preview(locale = "ru")
@Preview(locale = "en")
@Composable
fun PlaylistScreenContentPreview() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            PlaylistScreenContent(
                onMashupInfoClick = {},
                onBackClicked = { },
                onLikeClick = {},
                onMashupClick = {},
                onShuffleClick = {},
                onPlayClick = {},
                state = PlaylistScreenState(
                    currentlyPlayingMashupId = 22,
                    isMashupListLoading = false,
                    isLoading = false,
                    playlistInfo = Playlist(
                        1,
                        "Плейлист для качалки",
                        "",
                        listOf("Meonidlel"),
                        "964",
                        listOf(1,3,4,5),
                        322,
                        22
                    ),
                    mashupList = listOf(
                        MashupListItem(
                            12,
                            "Лобби под подошвой",
                            "Утонул в пиве",
                            "213",
                            false,
                            0,
                            1,
                            listOf(1,2),
                            false
                        ),
                        MashupListItem(
                            22,
                            "Лобби под подошвой",
                            "Утонул в пиве",
                            "213",
                            false,
                            0,
                            1,
                            listOf(1,2),
                            true
                        ),
                        MashupListItem(
                            32,
                            "Лобби под подошвой",
                            "Утонул в пиве",
                            "213",
                            false,
                            0,
                            1,
                            listOf(1,2),
                            true
                        ),
                        MashupListItem(
                            42,
                            "Лобби под подошвой",
                            "Утонул в пиве",
                            "213",
                            false,
                            0,
                            1,
                            listOf(1,2),
                            false
                        ),
                    )
                )
            )
        }
    }
}


@Preview(locale = "ru")
@Preview(locale = "en")
@Composable
fun PlaylistScreenContentPreviewAllLoading() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            PlaylistScreenContent(
                onMashupInfoClick = {},
                onBackClicked = { },
                onLikeClick = {},
                onMashupClick = {},
                onShuffleClick = {},
                onPlayClick = {},
                state = PlaylistScreenState(
                    isMashupListLoading = false,
                    isLoading = true,
                )
            )
        }
    }
}

@Preview(locale = "ru")
@Preview(locale = "en")
@Composable
fun PlaylistScreenContentPreviewMashupListLoading() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            PlaylistScreenContent(
                onMashupInfoClick = {},
                onBackClicked = { },
                onLikeClick = {},
                onMashupClick = {},
                onShuffleClick = {},
                onPlayClick = {},
                state = PlaylistScreenState(
                    isMashupListLoading = true,
                    isLoading = false,
                    playlistInfo = Playlist(
                        1,
                        "Плейлист для качалки",
                        "",
                        listOf("Meonidlel"),
                        "964",
                        listOf(1,3,4,5),
                        322,
                        22,
                        3947062,
                    ),
                )
            )
        }
    }
}

@Preview(locale = "ru")
@Preview(locale = "en")
@Composable
fun PlaylistScreenContentPreviewLoadingError() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            PlaylistScreenContent(
                onMashupInfoClick = {},
                onBackClicked = { },
                onLikeClick = {},
                onMashupClick = {},
                onShuffleClick = {},
                onPlayClick = {},
                state = PlaylistScreenState(
                    errorMessage = "Pizdec nahooy blyat",
                    currentlyPlayingMashupId = 22,
                    isMashupListLoading = false,
                    isLoading = false,
                )
            )
        }
    }
}