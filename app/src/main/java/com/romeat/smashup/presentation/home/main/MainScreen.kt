package com.romeat.smashup.presentation.home.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.presentation.home.common.composables.listitem.PlaylistItem
import com.romeat.smashup.util.SquareDisplayItem

@Composable
fun MainScreen(
    onPlaylistClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onMashupInfoClick: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_title),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(48.dp)
                    .padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_bell_empty),
                    contentDescription = "notifications"
                )
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(48.dp)
                    .padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings_button),
                    contentDescription = "settings"
                )
            }
        }
        Spacer(modifier = Modifier.size(20.dp))

        // Toggle buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            ButtonRoundedCorners(
                textRes = R.string.mashups_toggle_button,
                isToggled = true,
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(15.dp))

            ButtonRoundedCorners(
                textRes = R.string.shitpost_toggle_button,
                isToggled = false,
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.size(30.dp))

        // Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            if (state.isError) {
                ErrorTextMessage()
            } else if (state.isProgress) {
                CustomCircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ContentRow(
                                titleRes = R.string.charts,
                                itemList = state.playlists,
                                onItemClick = onPlaylistClick
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                    if (state.recommendations.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.playlist_recommendations),
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.h6.fontSize,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                        items(
                            items = state.recommendations,
                            key = { it.id }
                        ) { mashup ->
                            MashupItem(
                                mashup = mashup,
                                onBodyClick = { viewModel.onMashupClick(it.id) },
                                onInfoClick = { onMashupInfoClick(mashup.serializedMashup) },
                                onLikeClick = { id -> viewModel.onLikeClick(id)},
                                isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                                    ?: false
                            )
                        }
                    }

//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                    ) {
//                        if (state.liked.isNotEmpty()) {
//                            ContentRow(
//                                titleRes = R.string.charts,
//                                itemList = state.liked,
//                                onItemClick = { }
//                            )
//                        }
//                    }
                }
            }
        }
    }
}

@Composable
fun ContentRow(
    titleRes: Int,
    itemList: List<SquareDisplayItem>,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(titleRes),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h6.fontSize,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(itemList.size) { i ->
                SquareCard(
                    itemList[i],
                    onClick = { onItemClick(itemList[i].id) }
                )
            }
        }
    }
}

@Composable
fun ButtonRoundedCorners(
    textRes: Int,
    isToggled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(33),
        border = null,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (isToggled) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
    ) {
        Text(
            text = stringResource(id = textRes),
            fontSize = MaterialTheme.typography.body2.fontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}