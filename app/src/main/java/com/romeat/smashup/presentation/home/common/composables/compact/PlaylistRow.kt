package com.romeat.smashup.presentation.home.common.composables.compact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper.playlistImageIdToUrl400px

@Composable
fun PlaylistRow(
    playlists: List<Playlist>,
    onClick: (Int) -> Unit,
    onMoreClick: () -> Unit = { },
    maxNumberOfItemsToDisplay: Int = 4,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.playlists_title),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            if (playlists.size > maxNumberOfItemsToDisplay) {
                Text(
                    modifier = Modifier
                        .widthIn(min = 48.dp)
                        .clickable { onMoreClick() },
                    text = stringResource(id = R.string.all),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.End,
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(minOf(playlists.size, maxNumberOfItemsToDisplay)) { i ->
                val item = playlists[i]
                PlaylistRowItem(
                    id = item.id,
                    imageUrl = item.imageUrl,
                    title = item.name,
                    owner = item.owner,
                    onClick = { onClick(item.id) }
                )
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun RowPreview() {
    SmashupTheme() {
        Surface(color = MaterialTheme.colors.surface) {
            Column {
                PlaylistRow(playlists = listOf(
                    Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                    Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),

                ), onClick = {})

                PlaylistRow(playlists = listOf(
                    Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                    Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                    Playlist(14, "Yooooo", "", listOf("KEKW"), "def", listOf(),1,1),
                    Playlist(15, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                    ), onClick = {})

                PlaylistRow(playlists = listOf(
                    Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                    Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                    Playlist(14, "Playlist nei", "", listOf("KEKW"), "def", listOf(),1,1),
                    Playlist(15, "Yooooo", "", listOf("BRUH"), "def", listOf(),1,1),
                    Playlist(35, "Playlist dlya mochalki", "", listOf("KEKW"), "def", listOf(),1,1),
                ), onClick = {})
            }
        }
    }
}


@Composable
fun PlaylistRowItem(
    id: Int,
    imageUrl: String,
    title: String,
    owner: String,
    onClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable { onClick(id) }
    ) {
        FriendlyGlideImage(
            imageModel = playlistImageIdToUrl400px(imageUrl),
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp)),
            error = Placeholder.Playlist.resource,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(3.dp))
        if (owner.isNotBlank()) {
            Text(
                text = owner,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.size(3.dp))
        }
    }
}
