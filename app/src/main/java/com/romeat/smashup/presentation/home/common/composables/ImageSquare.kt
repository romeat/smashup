package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageSquare(
    url: String,
    placeholder: Placeholder = Placeholder.Napas
) {
    Box(modifier = Modifier
        .height(200.dp)
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            imageModel = { url },
            modifier = Modifier
                .size(200.dp),

//            contentScale = ContentScale.Crop,
//            error = ImageVector.vectorResource(id = placeholder.resource),
//            shimmerParams = ShimmerParams(
//                baseColor = MaterialTheme.colors.background,
//                highlightColor = MaterialTheme.colors.surface,
//                durationMillis = 700,
//                tilt = 0f
//            )
        )
    }
}

enum class Placeholder(val resource: Int) {
    SmashupDefault(R.drawable.smashup_default),
    Napas(R.drawable.napas),
    Playlist(R.drawable.ic_baseline_library_music_24),
    Track(R.drawable.ic_baseline_audiotrack_24),
    Source(R.drawable.ic_baseline_album_24)
}

@Preview
@Composable
fun ImageSquarePreview() {
    ImageSquare(url = "")
}