package com.romeat.smashup.presentation.home.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.toMashupUListItem
import com.romeat.smashup.presentation.home.common.composables.*


// todo
// (добавить переходы на полные списки)
// (добавить переход на юзера)
@Composable
fun SearchScreen(
    onMashupInfoClick: (Int) -> Unit,
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (Int) -> Unit,
    onPlaylistClick: (Int) -> Unit,
    viewModel: SearchBarViewModel = hiltViewModel()
) {
    val state by viewModel.resultState.collectAsState()
    val searchQueryState by viewModel.searchQueryState.collectAsState()
    val currentlyPlayingMashupId = viewModel.currentlyPlaying
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopRow(
            title = stringResource(R.string.search),
            onBackPressed = { },
            showBackButton = false
        )
        StyledInput(
            text = searchQueryState,
            onTextChange = { newValue -> viewModel.onQueryChange(newValue) },
            placeholderResId = R.string.search_hint,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            trailingIcon = {
                if (searchQueryState.isNotEmpty())
                    ClearInputTrailingIcon(onClick = { viewModel.clearInput() })
            },
            enabled = true,
            isError = false,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (state.isLoading) {
            CustomCircularProgressIndicator()
        } else if (state.isError) {
            ErrorTextMessage()
        } else if (state.isResultEmpty){
            ErrorTextMessage(R.string.search_nothing_found)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.mashups.isNotEmpty()) {
                    item {
                        MashupListCompact(
                            mashups = state.mashups,
                            onMashupClick = viewModel::onMashupClick,
                            onLikeClick = viewModel::onLikeClick,
                            onMashupInfoClick = onMashupInfoClick,
                            currentlyPlayingMashupId = currentlyPlayingMashupId
                        )
                    }
                }
                if (state.users.isNotEmpty()) {
                    item {
                        UserListCompact(
                            users = state.users,
                            onClick = onAuthorClick
                        )
                    }
                }
                if (state.playlists.isNotEmpty()) {
                    item {
                        PlaylistRow(
                            playlists = state.playlists,
                            onClick = onPlaylistClick
                        )
                    }
                }
                if (state.sources.isNotEmpty()) {
                    item {
                        SourceListCompact(
                            sources = state.sources,
                            onClick = onSourceClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClearInputTrailingIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "clear field")
    }
}
