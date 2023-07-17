package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun PlayableHeader(
    imageUrl: String,
    title: String,
    mashupsCount: Int,

    onPlayPauseClick: () -> Unit,
    onShuffleClick: () -> Unit,

    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onSubtitleClick: () -> Unit = {},
    subtitleClickable: Boolean = true,

    onMoreClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Spacer for overlaying top row
        Spacer(modifier = Modifier.height(70.dp))
        // Image and titles
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            FriendlyGlideImage(
                imageModel = imageUrl,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp)),
                error = Placeholder.Playlist.resource,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.h5,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubtitleClick() },
                        text = it,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal,
                        textDecoration = if (subtitleClickable) TextDecoration.Underline else TextDecoration.None
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = pluralStringResource(id = R.plurals.mashups_number, mashupsCount, mashupsCount),
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Play, shuffle and other buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = onMoreClick
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    imageVector = ImageVector
                        .vectorResource(id = R.drawable.ic_more_button),
                    contentDescription = "more"
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onShuffleClick,
                modifier = Modifier
                    .size(48.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_shuffle_button),
                    contentDescription = "shuffle"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(48.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_play_circle_button),
                    contentDescription = "play"
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Preview
@Composable
fun PlaylistHeaderPreview() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            Column {

                // playlist
                PlayableHeader(
                    imageUrl = "123",
                    title = "Плейлист для качалки",
                    mashupsCount = 32,
                    subtitle = "SmashUp",
                    subtitleClickable = true,
                    onPlayPauseClick = { /*TODO*/ },
                    onShuffleClick = { /*TODO*/ }
                )

                // profile
                PlayableHeader(
                    imageUrl = "123",
                    title = "warkka",
                    mashupsCount = 32,
                    onPlayPauseClick = { /*TODO*/ },
                    onShuffleClick = { /*TODO*/ }
                )

                // source page
                PlayableHeader(
                    imageUrl = "123",
                    title = "Я сижу на сочинском пляжу и на воду синюю гляжу",
                    mashupsCount = 32,
                    subtitle = "Vdrug ko mne podhodit hrenoten",
                    subtitleClickable = false,
                    onPlayPauseClick = { /*TODO*/ },
                    onShuffleClick = { /*TODO*/ }
                )
            }
        }
    }
}