package com.romeat.smashup.presentation.home.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.presentation.home.common.composables.CustomCircularProgressIndicator
import com.romeat.smashup.presentation.home.common.composables.ErrorTextMessage
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.presentation.home.common.composables.listitem.PlaylistItem
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.compose.BackPressHandler

// todo swipe to refresh
@Composable
fun CollectionScreen(
    onBackClick: () -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    viewModel: CollectionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        CollectionScreenContent(
            state = state,
            onBackClick = onBackClick,
            onPlaylistClick = onPlaylistClick,
            onMashupInfoClick = onMashupInfoClick,
            onMashupClick = viewModel::onMashupClick,
            onMashupLikeClick = viewModel::onLikeClick
        )
    }
}

@Composable
fun CollectionScreenContent(
    state: CollectionState,
    onBackClick: () -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onMashupClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onMashupLikeClick: (Int) -> Unit,
) {
    val mashupListOpened = remember { mutableStateOf(false) }

    BackPressHandler(
        onBackPressed = {
            if (mashupListOpened.value) {
                mashupListOpened.value = false
            } else {
                onBackClick()
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // all liked mashups overlay
        if (mashupListOpened.value) {
            MashupFullListOverlay(
                mashupListOpened = mashupListOpened,
                state = state,
                onMashupClick = onMashupClick,
                onMashupInfoClick = onMashupInfoClick,
                onLikeClick = onMashupLikeClick
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopRow(
                title = stringResource(R.string.bottom_bar_collection),
                onBackPressed = { },
                showBackButton = false
            )
            if (state.isError) {
                ErrorTextMessage()
            } else if (state.isLoading) {
                CustomCircularProgressIndicator()
            }  else if (state.isEmpty) {
                ErrorTextMessage(R.string.collection_empty)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (state.myLikedMashups.isNotEmpty()) {
                        item {
                            LikedMashupsPlaylist(
                                onClick = { mashupListOpened.value = true },
                                mashupsCount = state.myLikedMashups.size
                            )
                        }
                    }

                    if (state.myPlaylists.isNotEmpty()) {
                        items(
                            items = state.myPlaylists,
                            key = { it.id }
                        ) { playlist ->
                            PlaylistItem(playlist = playlist, onClick = { onPlaylistClick(playlist.id) })
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun LikedMashupsPlaylist(
    onClick: () -> Unit,
    mashupsCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector
                    .vectorResource(id = R.drawable.ic_heart_filled),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        }
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
                    text = stringResource(id = R.string.collection_favourite_mashups),
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
                    text = pluralStringResource(id = R.plurals.mashups_number, mashupsCount, mashupsCount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Composable
fun MashupFullListOverlay(
    mashupListOpened: MutableState<Boolean>,
    state: CollectionState,
    onMashupClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopRow(title = stringResource(id = R.string.collection_favourite_mashups), onBackPressed = { mashupListOpened.value = false })
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(
                        items = state.myLikedMashups,
                        key = { it.id }
                    ) { mashup ->
                        MashupItem(
                            mashup = mashup,
                            onBodyClick = { onMashupClick(it.id) },
                            onInfoClick = onMashupInfoClick,
                            onLikeClick = onLikeClick,
                            isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                                ?: false
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CollectionScreenContentPreview() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            CollectionScreenContent(
                state = CollectionState(
                    false,
                    false,
                    null,
                    listOf(),
                    listOf(
                        MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                        MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
                        MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                        MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
                        MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                        MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
                    ),
                    true,
                    listOf(),
                    true
                ),
                {}, {}, {}, {}, {}
            )
        }
    }
}