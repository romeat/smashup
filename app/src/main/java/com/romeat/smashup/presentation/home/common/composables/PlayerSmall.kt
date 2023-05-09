package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.PlayerState
import com.romeat.smashup.ui.theme.SmashupTheme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun PlayerSmall(
    viewModel: HomePlayerViewModel,
    onExpandClick: () -> Unit
) {
    PlayerSmallContent(
        state = viewModel.state.collectAsState().value,
        onPreviousClick = { viewModel.onPreviousClick() },
        onPlayPauseClick = { viewModel.onPlayPauseClick() },
        onNextClick = { viewModel.onNextClick() },
        onLikeClick = viewModel::onLikeClick,
        onExpandClick = onExpandClick
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerSmallContent(
    state: PlayerState,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onExpandClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    if (state.isPlaybackNull) return

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable { onExpandClick() }
    ) {
        val gradientColorToTransparent = List(4) {
            MaterialTheme.colors.surface
        }.plus(Color.Transparent)

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            progress = 0f
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .background(Brush.horizontalGradient(gradientColorToTransparent)),
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 3.dp)
                        .size(60.dp),
                    onClick = onPlayPauseClick,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_play_button),
                        contentDescription = "play"
                    )
                }
            }

            var positionOffset by remember { mutableStateOf(0f) }
            var infoVisible by remember { mutableStateOf(true) }
            val infoAlpha: Float by animateFloatAsState(
                targetValue = if (infoVisible) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 10,
                    easing = FastOutLinearInEasing,
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .alpha(infoAlpha)
                    .offset { IntOffset(positionOffset.roundToInt(), 0) }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = DraggableState { delta ->
                            positionOffset += delta
                        },
                        onDragStopped = {
                            if (positionOffset.absoluteValue > 50) {
                                infoVisible = false
                                if (positionOffset > 0) {
                                    onPreviousClick()
                                } else {
                                    onNextClick()
                                }
                                positionOffset = 0f
                            }
                        }
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedContent(
                        targetState = state.trackName,
                        transitionSpec = { fadeIn() with fadeOut() }
                    ) { trackName ->
                        Text(
                            text = trackName,
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body1
                        )
                        infoVisible = true
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    AnimatedContent(
                        targetState = state.trackAuthor,
                        transitionSpec = { fadeIn() with fadeOut() }
                    ) { author ->
                        Text(
                            text = author,
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .background(Brush.horizontalGradient(gradientColorToTransparent.reversed())),
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 10.dp)
                        .size(60.dp),
                    onClick = onLikeClick,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        imageVector = if (state.isLiked) {
                            ImageVector.vectorResource(R.drawable.ic_heart_filled)
                        } else {
                            ImageVector.vectorResource(R.drawable.ic_heart_border)
                        },
                        contentDescription = "like"
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PlayerSmallPreview() {
    SmashupTheme() {
        PlayerSmallContent(
            state = PlayerState(isPlaybackNull = false),
            onPreviousClick = { /*TODO*/ },
            onPlayPauseClick = { /*TODO*/ },
            onNextClick = { /*TODO*/ },
            onExpandClick = { },
            onLikeClick = { },
        )
    }
}
