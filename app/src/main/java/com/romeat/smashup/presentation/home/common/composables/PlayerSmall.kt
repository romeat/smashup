package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.util.compose.Marquee
import com.romeat.smashup.util.compose.MarqueeParams
import com.romeat.smashup.util.toDisplayableTimeString
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayerSmall(
    viewModel: HomePlayerViewModel,
    onExpandClick: () -> Unit
    /*
    onPlayClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,

    state: PlayerState,
    currentTimeMs: Long

     */
) {
    val state = viewModel.state
    Column(
        modifier = Modifier
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
                .padding(horizontal = 2.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = state.trackAuthor + " - " + state.trackName,
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
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .width(48.dp)
            ) {
                GlideImage(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    contentScale = ContentScale.Crop,
                    imageModel = ImageUrlHelper.mashupImageIdToUrl100px(state.imageId.toString()) ,
                            contentDescription = "track cover small",
                            error = ImageVector.vectorResource(id = Placeholder.Track.resource),
                            shimmerParams = ShimmerParams(
                                baseColor = MaterialTheme.colors.background,
                                highlightColor = MaterialTheme.colors.surface,
                                durationMillis = 700,
                                tilt = 0f
                            )
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = viewModel.currentTimeMs.toDisplayableTimeString(),
                            fontSize = 10.sp
                        )
            }

            Row(
                modifier = Modifier
                    .weight(1.0f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    onClick = { viewModel.onPreviousClick() },
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
                        .width(60.dp)
                        .height(54.dp),
                    onClick = { viewModel.onPlayPauseClick() },
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = if (state.isPlaying) {
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
                        .width(48.dp)
                        .height(48.dp),
                    onClick = { viewModel.onNextClick() },
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_skip_next_24),
                        contentDescription = "next track"
                    )
                }
            }

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
            progress = viewModel.currentTimeMs.toFloat()/state.trackDurationMs,
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

/*
@Preview
@Composable
fun PlayerPreview() {
    PlayerSmall(
        onPlayClick = { /*TODO*/ },
        onPreviousClick = { /*TODO*/ },
        onNextClick = { /*TODO*/ },
        onExpandClick = { /*TODO*/ },
        state = PlayerState(),
        currentTimeMs = 11000
    )
}
 */