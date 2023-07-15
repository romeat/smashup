package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.glide.GlideImage
import com.romeat.smashup.R
import com.romeat.smashup.ui.theme.SmashupTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .clickable { onClick(playlist.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        FriendlyGlideImage(
            imageModel = ImageUrlHelper.playlistImageIdToUrl400px(playlist.imageUrl),
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(15.dp)),
            error = Placeholder.Playlist.resource,
        )
        Spacer(modifier = Modifier.size(20.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .weight(1.0f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = playlist.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pluralStringResource(id = R.plurals.mashups_number, count = playlist.mashups.size),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Preview
@Composable
fun PlaylistItemPreview() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            PlaylistItem(
                playlist = Playlist(
                    12,
                    "Плейлист для качалки",
                    "",
                    listOf("das","dopO"),
                    "def",
                    listOf(1,2,3,4),
                    1,
                    4
                ),
                onClick = {})
        }
    }
}