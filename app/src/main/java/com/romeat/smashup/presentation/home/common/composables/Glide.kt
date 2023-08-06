package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun FriendlyGlideImage(
    imageModel: String,
    modifier: Modifier,
    error: Int,
    composePreviewRes: Int = R.drawable.smashup_default
) {
    GlideImage(
        imageModel = { imageModel },
        modifier = modifier.background(color = MaterialTheme.colors.surface),
        previewPlaceholder = composePreviewRes,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        ),
        component = rememberImageComponent {
            +PlaceholderPlugin.Failure(painterResource(id = error))
            +ShimmerPlugin(
                baseColor = MaterialTheme.colors.background,
                highlightColor = MaterialTheme.colors.surface,
                durationMillis = 700,
                tilt = 0f
            )
        }
    )
}

enum class Placeholder(val resource: Int) {
    SmashupDefault(R.drawable.smashup_default),
    Napas(R.drawable.napas),
    Playlist(R.drawable.ic_baseline_library_music_24),
    Source(R.drawable.ic_baseline_album_24)
}
