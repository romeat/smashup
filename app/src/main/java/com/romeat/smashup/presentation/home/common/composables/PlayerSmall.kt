package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.PlayerState
import com.romeat.smashup.util.ConcatAuthorAndTitle
import com.romeat.smashup.util.compose.Marquee
import com.romeat.smashup.util.compose.MarqueeParams
import com.romeat.smashup.util.toDisplayableTimeString

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayerSmall(
    viewModel: HomePlayerViewModel,
    onExpandClick: () -> Unit
) {
    PlayerSmallContent(
        state = viewModel.state.collectAsState().value,
        currentTimeMs = viewModel.currentTimeMs,
        onPreviousClick = { viewModel.onPreviousClick() },
        onPlayPauseClick = { viewModel.onPlayPauseClick() },
        onNextClick = { viewModel.onNextClick() },
        onExpandClick = onExpandClick
    )
}

@Composable
fun PlayerSmallContent(
    state: PlayerState,
    currentTimeMs: Long,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onExpandClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20, 20, 0, 0))
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
    ) {
        Marquee(
            params = MarqueeParams(
                period = 7500,
                gradientEnabled = false,
                gradientEdgeColor = Color.Transparent,
                direction = LocalLayoutDirection.current,
                easing = LinearEasing
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = ConcatAuthorAndTitle(state.trackAuthor, state.trackName),
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.body2.fontSize,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .width(48.dp)
            ) {
                TrackCoverSmall(image = state.coverSmall)
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    text = currentTimeMs.toDisplayableTimeString(),
                    fontSize = 10.sp
                )
            }

            PlaybackControlsRow(
                onPreviousClick = onPreviousClick,
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                isPlaybackNull = state.isPlaybackNull,
                isPlaying = state.isPlaying,
                modifier = Modifier.weight(1.0f)
            )

            Column(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .width(48.dp)
            ) {
                IconButton(
                    enabled = !state.isPlaybackNull,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    onClick = onExpandClick,
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "expand player"
                    )
                }
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    text = state.trackDurationMs.toDisplayableTimeString(),
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(),
            progress = currentTimeMs.toFloat() / state.trackDurationMs,
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
fun TrackCoverSmall(image: ImageBitmap?) {
    Box(modifier = Modifier.size(48.dp)) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .alpha(ContentAlpha.disabled),
            imageVector = ImageVector.vectorResource(id = R.drawable.napas),
            contentDescription = ""
        )
        if (image != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = image, contentDescription = "")
        }
    }
}

@Composable
fun PlaybackControlsRow(
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    isPlaybackNull: Boolean,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f),
            enabled = !isPlaybackNull,
            onClick = onPreviousClick,
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_skip_previous_24),
                contentDescription = "previous track"
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f),
            enabled = !isPlaybackNull,
            onClick = onPlayPauseClick,
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = if (isPlaying) {
                    ImageVector.vectorResource(id = R.drawable.ic_baseline_pause_24)
                } else {
                    ImageVector.vectorResource(id = R.drawable.ic_baseline_play_arrow_24)
                },
                contentDescription = "play/pause"
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f),
            enabled = !isPlaybackNull,
            onClick = onNextClick,
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_skip_next_24),
                contentDescription = "next track"
            )
        }
    }
}

@Composable
@Preview
fun PlayerSmallPreview() {
    PlayerSmallContent(
        state = PlayerState(),
        currentTimeMs = 22000,
        onPreviousClick = { /*TODO*/ },
        onPlayPauseClick = { /*TODO*/ },
        onNextClick = { /*TODO*/ },
        onExpandClick = { }
    )
}
