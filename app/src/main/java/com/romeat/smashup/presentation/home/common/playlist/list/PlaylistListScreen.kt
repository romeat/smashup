package com.romeat.smashup.presentation.home.common.playlist.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*

@Composable
fun PlaylistListScreen(
    onBackClick: () -> Unit,
    onPlaylistClick: (Int) -> Unit,
    viewModel: PlaylistListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopRow(
                title = stringResource(R.string.playlists_title),
                onBackPressed = onBackClick,
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    item { CustomCircularProgressIndicator() }
                } else if (state.errorMessage.isNotBlank()) {
                    item { ErrorTextMessage() }
                } else {
                    items(
                        items = state.playlists,
                        key = { it.id }
                    ) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            onClick = { onPlaylistClick(playlist.id) },
                        )
                    }
                }
            }
        }
    }
}