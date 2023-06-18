package com.romeat.smashup.presentation.home.common.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.R

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
        TopRow(
            title = "",
            onBackPressed = onBackClicked
        )
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { viewModel.onPlayClick() },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(90.dp)
                                    .background(
                                        color = MaterialTheme.colors.onSurface,
                                        shape = RoundedCornerShape(11.dp)
                                    )
                                    .padding(13.dp),
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_standard_button),
                                    tint = MaterialTheme.colors.background,
                                    contentDescription = "play"
                                )
                            }
                            Spacer(modifier = Modifier.width(25.dp))
                            IconButton(
                                onClick = { viewModel.onShuffleClick() },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(90.dp)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colors.onSurface,
                                        shape = RoundedCornerShape(11.dp)
                                    )
                                    .background(color = Color.Transparent)
                                    .padding(13.dp),
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_shuffle_button),
                                    tint = MaterialTheme.colors.onSurface,
                                    contentDescription = "shuffle"
                                )
                            }
                        }
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
                                onBodyClick = { viewModel.onMashupClick(it.id) },
                                onInfoClick = { id -> onMashupInfoClick(id) },
                                onLikeClick = { id -> viewModel.onLikeClick(id)},
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