package com.romeat.smashup.presentation.home.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R
import com.romeat.smashup.musicservice.PlaybackRepeatMode
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.PlayerState
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.util.toDisplayableTimeString


@Composable
fun AudioPlayerScreen(
    onBackPressed: () -> Unit,
    viewModel: HomePlayerViewModel
) {
    AudioPlayerContent(
        onBackPressed = { onBackPressed() },
        state = viewModel.state.collectAsState().value,
        timestamp = viewModel.currentTimeMs,
        onPreviousClick = { viewModel.onPreviousClick() },
        onNextClick = { viewModel.onNextClick() },
        onPlayPauseClick = { viewModel.onPlayPauseClick() },
        onShuffleClick = { viewModel.onShuffleClick() },
        onRepeatClick = { viewModel.onRepeatClick() },
        onLikeClick = { viewModel.onLikeClick() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioPlayerContent(
    state: PlayerState,
    timestamp: Long,
    onBackPressed: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
        ) {
            /* Top row */
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    onClick = onBackPressed
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .rotate(270f),
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_chevron_left_button),
                        contentDescription = "hide player",
                    )
                }

                Text(
                    text = state.playlistTitle,
                    style = MaterialTheme.typography.h5,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(10)
                )

                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    onClick = { }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_more_button),
                        contentDescription = "more",
                    )
                }
            }

            /* Cover */
            Row(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                FriendlyGlideImage(
                    imageModel = ImageUrlHelper.mashupImageIdToUrl400px(state.id.toString()),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15))
                        .aspectRatio(1.0f),
                    error = Placeholder.Napas.resource,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /* Author & track name */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = state.trackName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .basicMarquee(10)
                    )

                    Text(
                        text = state.trackAuthor,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .basicMarquee(10)
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    onClick = onLikeClick
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        imageVector = if (state.isLiked) {
                            ImageVector.vectorResource(R.drawable.ic_heart_filled)
                        } else {
                            ImageVector.vectorResource(R.drawable.ic_heart_border)
                        },
                        contentDescription = "like",
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* Progress */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(),
                        progress = timestamp.toFloat() / state.trackDurationMs,
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = timestamp.toDisplayableTimeString(),
                            fontSize = 10.sp
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = state.trackDurationMs.toDisplayableTimeString(),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* Controls */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1.0f),
                    onClick = onRepeatClick
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = ImageVector
                            .vectorResource(
                                id =
                                when (state.repeatMode) {
                                    PlaybackRepeatMode.None -> R.drawable.ic_repeat_off_button
                                    PlaybackRepeatMode.RepeatOneSong -> R.drawable.ic_repeat_one_button
                                    PlaybackRepeatMode.RepeatPlaylist -> R.drawable.ic_repeat_all_button
                                }
                            ),
                        contentDescription = "repeat"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1.0f),
                    onClick = onPreviousClick
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_previous_standard_button),
                        contentDescription = "previous track"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1.0f),
                    onClick = onPlayPauseClick
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = if (state.isPlaying) {
                            ImageVector.vectorResource(id = R.drawable.ic_pause_standard_button)
                        } else {
                            ImageVector.vectorResource(id = R.drawable.ic_play_standard_button)
                        },
                        contentDescription = "play/pause"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1.0f),
                    onClick = onNextClick
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_next_standard_button),
                        contentDescription = "next track",
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1.0f),
                    onClick = onShuffleClick
                ) {
                    val color = LocalContentColor.current
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shuffle_button),
                        contentDescription = "shuffle",
                        tint = if (state.isShuffle) MaterialTheme.colors.primaryVariant else color
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AudioPlayerBigPreview() {
    SmashupTheme() {
        AudioPlayerContent(
            onBackPressed = { /*TODO*/ },
            state = PlayerState(),
            timestamp = 12000L,
            onPreviousClick = { /*TODO*/ },
            onNextClick = { /*TODO*/ },
            onPlayPauseClick = { },
            onShuffleClick = { },
            onRepeatClick = { },
            onLikeClick = { },
        )

    }
}

