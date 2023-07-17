package com.romeat.smashup.presentation.home.common.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.presentation.home.common.composables.CustomCircularProgressIndicator
import com.romeat.smashup.presentation.home.common.composables.ErrorTextMessage
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem
import com.romeat.smashup.presentation.home.common.composables.compact.MashupListCompact
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.presentation.home.common.composables.compact.PlaylistRow
import com.romeat.smashup.presentation.home.common.composables.TextBody1
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.presentation.home.common.composables.TransparentTopRow
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.util.compose.BackPressHandler

@Composable
fun UserScreen(
    onMashupInfoClick: (Int) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        UserScreenContent(
            state = state,
            onMashupInfoClick = onMashupInfoClick,
            onPlaylistClick = onPlaylistClick,
            onMashupClick = viewModel::onMashupClick,
            onLikeClick = viewModel::onLikeClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun UserScreenContent(
    state: UserScreenState,
    onMashupInfoClick: (Int) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    onMashupClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onBackClick: () -> Unit,
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

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (mashupListOpened.value) {
            MashupFullListOverlay(
                mashupListOpened = mashupListOpened,
                state = state,
                onMashupClick = onMashupClick,
                onMashupInfoClick = onMashupInfoClick,
                onLikeClick = onLikeClick
            )
        }

        TransparentTopRow(
            onBackPressed = onBackClick,
            modifier = Modifier.zIndex(2f)
        )
        if (state.isLoading || !state.mashupsLoaded || !state.playlistsLoaded) {
            CustomCircularProgressIndicator()
        } else if (state.errorMessage.isNotBlank()) {
            ErrorTextMessage()
        } else {
            val info = state.userInfo!!
            LazyColumn() {
                item {
                    UserInfoHeader(
                        imageUrl = ImageUrlHelper.authorImageIdToUrl400px(info.imageUrl),
                        title = info.username,
                        mashupsCount = state.mashupList.size,
                    )
                }
                if (state.isEmpty) {
                    item {
                        TextBody1(resId = R.string.profile_no_content, modifier = Modifier.padding(15.dp))
                    }
                } else {
                    if (state.mashupList.isNotEmpty()) {
                        item {
                            MashupListCompact(
                                mashups = state.mashupList,
                                onMashupClick = onMashupClick,
                                onLikeClick = onLikeClick,
                                onMashupInfoClick = onMashupInfoClick,
                                onMoreClick = { mashupListOpened.value = true }
                            )
                        }
                    }
                    if (state.playlistList.isNotEmpty()) {
                        item {
                            PlaylistRow(
                                playlists = state.playlistList,
                                onClick = onPlaylistClick,
                                maxNumberOfItemsToDisplay = state.playlistList.size
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(25.dp)) }
            }
        }
    }
}

@Composable
fun MashupFullListOverlay(
    mashupListOpened: MutableState<Boolean>,
    state: UserScreenState,
    onMashupClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(3f)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopRow(title = stringResource(id = R.string.all_mashups), onBackPressed = { mashupListOpened.value = false })
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.mashupList.size) { i ->
                        val mashup = state.mashupList[i]
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
fun UserScreenContentPreview(
    @PreviewParameter(UserScreenStateProvider::class) state: UserScreenState
) {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            UserScreenContent(
                state = state,
                onMashupInfoClick = {},
                onPlaylistClick = {},
                onMashupClick = {},
                onLikeClick = {},
                onBackClick = {}
            )
        }
    }
}

class UserScreenStateProvider : PreviewParameterProvider<UserScreenState> {
    override val values = listOf(
        UserScreenState(), // initial
        UserScreenState(isLoading = false, userInfo = UserProfile(1, "murrka", "", 1, listOf(), listOf())), // loaded profile only
        UserScreenState(isLoading = false, isEmpty = true,  userInfo = UserProfile(1, "murrka", "", 1, listOf(), listOf())), // loaded all, but empty lists
        UserScreenState(isLoading = false, errorMessage = "error"), // load error
        UserScreenState(isLoading = false, errorMessage = "error", mashupsLoaded = true), // load error because of list
        UserScreenState( // mashups loaded, playlists not yet loaded
            isLoading = false,
            mashupsLoaded = true,
            userInfo = UserProfile(1, "murrka", "def", 1, listOf(), listOf()),
            mashupList = listOf(
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(15, "popandos", "lavandos", "null", false, 1, 1, emptyList(), false),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
            )
        ),
        UserScreenState( // mashups loaded, playlists loaded and empty
            isLoading = false,
            mashupsLoaded = true,
            userInfo = UserProfile(1, "murrka", "def", 1, listOf(), listOf()),
            mashupList = listOf(
                MashupListItem(12, "popandos", "lavandos", "null", false, 1, 1, emptyList(), true),
                MashupListItem(15, "popandos", "lavandos", "null", false, 1, 1, emptyList(), false),
                MashupListItem(13, "joss", "lavandos", "null", false, 1, 1, emptyList(), false),
            ),
            playlistsLoaded = true
        ),
        UserScreenState( // mashups loaded, playlists loaded
            isLoading = false,
            mashupsLoaded = true,
            userInfo = UserProfile(1, "murrka", "def", 1, listOf(1, 2), listOf()),
            mashupList = listOf(
                MashupListItem(12, "popandos", "lavandos", "def", false, 1, 1, emptyList(), true),
                MashupListItem(15, "popandos", "lavandos", "def", false, 1, 1, emptyList(), false),
                MashupListItem(13, "joss", "lavandos", "def", false, 1, 1, emptyList(), false),
            ),
            playlistsLoaded = true,
            playlistList = listOf(
                Playlist(12, "Playlist dlya kachalki", "", listOf("bruh"), "def", listOf(),1,1),
                Playlist(13, "Playlist ne dlya kachalki", "", listOf("KEKW"), "def", listOf(),1,1),
                Playlist(14, "Playlist nei", "", listOf("KEKW"), "def", listOf(),1,1),
                Playlist(15, "Yooooo", "", listOf("BRUH"), "def", listOf(),1,1),
                Playlist(35, "Playlist dlya mochalki", "", listOf("KEKW"), "def", listOf(),1,1),
            )
        ),
    ).asSequence()
}

@Composable
fun UserInfoHeader(
    imageUrl: String,
    title: String,
    mashupsCount: Int,

    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
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
                error = Placeholder.Napas.resource,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h5,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = pluralStringResource(id = R.plurals.mashups_number, mashupsCount, mashupsCount),
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}
