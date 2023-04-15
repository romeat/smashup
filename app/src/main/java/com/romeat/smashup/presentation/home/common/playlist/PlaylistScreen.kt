package com.romeat.smashup.presentation.home.common.playlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun PlaylistScreen(
    onMashupInfoClick: (Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopRow(onBackPressed = onBackClicked)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else if (!state.errorMessage.isNullOrBlank()) {
                ErrorTextMessage()
            } else {
                val info = state.playlistInfo!!
                LazyColumn() {
                    item {
                        ImageSquare(url = ImageUrlHelper.playlistImageIdToUrl400px(info.imageUrl))
                        ContentDescription(content = info.name)
                        ClickableDescription(
                            name = info.owner,
                            onNameClick = { onAuthorClick(info.owner) })
                        StatsRow(likes = info.likes, listens = info.streams)
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
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
                            ListLoadingError()
                        }
                    } else {
                        items(
                            items = state.mashupList,
                            key = { it -> it.id }
                        ) { mashup ->
                            MashupItem(
                                mashup = mashup,
                                onBodyClick = { viewModel.onMashupClick(it) },
                                onInfoClick = { id -> onMashupInfoClick(id) },
                                isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                                    ?: false
                            )
                        }
                    }
                }
            }
        }
    }
}