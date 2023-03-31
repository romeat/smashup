package com.romeat.smashup.presentation.home.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.PlayerState
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.util.compose.Marquee
import com.romeat.smashup.util.compose.MarqueeParams
import com.romeat.smashup.util.toDisplayableTimeString
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun AudioPlayerScreen(
    onBackPressed: () -> Unit,
    viewModel: HomePlayerViewModel
) {
    AudioPlayerContent(
        onBackPressed = { onBackPressed() },
        state = viewModel.state,
        timestamp = viewModel.currentTimeMs,
        onPreviousClick = { viewModel.onPreviousClick() },
        onNextClick = { viewModel.onNextClick() },
        onPlayPauseClick = { viewModel.onPlayPauseClick() }
    )
}

@Composable
fun AudioPlayerContent(
    state: PlayerState,
    timestamp: Long,
    onBackPressed: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayPauseClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable { onBackPressed() },
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector
                        .vectorResource(id = R.drawable.ic_baseline_expand_more_24),
                    contentDescription = "hide player",
                    modifier = Modifier
                        .width(64.dp)
                        .height(32.dp)
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                GlideImage(
                    imageModel = ImageUrlHelper.mashupImageIdToUrl400px(state.imageId.toString()),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .aspectRatio(1.0f),
                    contentScale = ContentScale.Crop,
                    error = ImageVector.vectorResource(id = Placeholder.Napas.resource),
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = MaterialTheme.colors.surface,
                        durationMillis = 700,
                        tilt = 0f
                    )
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(0.01f))

            Marquee(
                params = MarqueeParams(
                    period = 7500,
                    gradientEnabled = false,
                    gradientEdgeColor = Color.Transparent,
                    direction = LocalLayoutDirection.current,
                    easing = LinearEasing),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.trackName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Marquee(
                params = MarqueeParams(
                    period = 7500,
                    gradientEnabled = false,
                    gradientEdgeColor = Color.Transparent,
                    direction = LocalLayoutDirection.current,
                    easing = LinearEasing),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.trackAuthor,
                    //maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.02f),
                )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                progress = timestamp.toFloat()/state.trackDurationMs,
                color = MaterialTheme.colors.primaryVariant
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = timestamp.toDisplayableTimeString(),
                    fontSize = 14.sp
                )

                Text(
                    textAlign = TextAlign.Center,
                    text = state.trackDurationMs.toDisplayableTimeString(),
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(5.dp)
                        .aspectRatio(1.0f),
                    onClick = { onPreviousClick() }
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_skip_previous_24),
                        contentDescription = "previous track"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .weight(1.2f)
                        .aspectRatio(1.0f),
                    onClick = { onPlayPauseClick() }
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
                IconButton(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(5.dp)
                        .aspectRatio(1.0f),
                    onClick = { onNextClick() }
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_skip_next_24),
                        contentDescription = "next track",
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f))
        }
    }
}

@Preview
@Composable
fun AudioPlayerBigPreview() {
    AudioPlayerContent(
        onBackPressed = { /*TODO*/ },
        state = PlayerState(),
        timestamp = 12000L,
        onPreviousClick = { /*TODO*/ },
        onNextClick = { /*TODO*/ },
        onPlayPauseClick = { }
    )
}

