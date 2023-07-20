package com.romeat.smashup.presentation.home.common.source

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun SourceScreen(
    onMashupInfoClick: (Int) -> Unit,
    onBackClicked: () -> Unit,
    viewModel: SourceViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        SourceScreenContent(
            onMashupInfoClick = onMashupInfoClick,
            onAuthorClick = { },
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
fun SourceScreenContent(
    onMashupInfoClick: (Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    onLikeClick: (Int) -> Unit,
    onMashupClick: (Int) -> Unit,
    onShuffleClick: () -> Unit,
    onPlayClick: () -> Unit,
    state: SourceScreenState,
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
        if (state.isLoading) {
            CustomCircularProgressIndicator()
        } else if (state.errorMessage.isNotBlank()) {
            ErrorTextMessage()
        } else {
            val info = state.sourceInfo!!
            LazyColumn(state = scrollState) {
                item {
                    PlayableHeader(
                        imageUrl = ImageUrlHelper.playlistImageIdToUrl400px(info.imageUrl),
                        title = info.name,
                        subtitle = info.owner,
                        subtitleClickable = false,
                        mashupsCount = state.mashupList.size,
                        onPlayPauseClick = onPlayClick,
                        onShuffleClick = onShuffleClick,
                        backgroundColor = info.backgroundColor,
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
                } else {
                    items(
                        items = state.mashupList,
                        key = { it.id }
                    ) { mashup ->
                        MashupItem(
                            mashup = mashup,
                            onBodyClick = { onMashupClick(it.id) },
                            onInfoClick = { id -> onMashupInfoClick(id) },
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