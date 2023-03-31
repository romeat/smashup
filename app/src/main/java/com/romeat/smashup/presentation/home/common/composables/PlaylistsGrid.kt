package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun PlaylistsGrid(
    playlists: List<Playlist>,
    onPlaylistClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
    ) {
        items(playlists.size) { i ->
            val playlist = playlists[i]
            PlaylistPreview(
                imageUrl = ImageUrlHelper.playlistImageIdToUrl400px(playlist.imageUrl),
                title = playlist.name,
                owner = playlist.owner,
                onClick = { onPlaylistClick(playlist.id) }
            )
        }
    }
}