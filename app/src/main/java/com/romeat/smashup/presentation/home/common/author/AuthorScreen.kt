package com.romeat.smashup.presentation.home.common.author

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun AuthorScreen(
    onMashupInfoClick: (Int) -> Unit,
    onBackClicked: () -> Unit,
    viewModel: AuthorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopRow(
            title = "",
            onBackPressed = onBackClicked
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else if (!state.errorMessage.isNullOrBlank()) {
                ErrorTextMessage()
            } else {
                val info = state.authorInfo!!
                LazyColumn() {
                    item {
                        ImageSquare(url = ImageUrlHelper.authorImageIdToUrl400px(info.imageUrl))
                        ContentDescription(content = info.username)
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(modifier = Modifier.padding(horizontal = 10.dp))
                    }
                    if (state.isMashupListLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                CustomCircularProgressIndicator()
                            }
                        }
                    } else if (state.isMashupListError) {
                        item {
                            ListLoadingError()
                        }
                    } else if (state.mashupList.isEmpty()) {
                        item {
                            ListLoadingError(stringResource(id = R.string.user_has_no_mashups))
                        }
                    } else {
                        items(
                            items = state.mashupList,
                            key = { it -> it.id }
                        ) { mashup ->
                            MashupItem(
                                mashup = mashup,
                                onBodyClick = { viewModel.onMashupClick(it.id) },
                                onInfoClick = { id -> onMashupInfoClick(id) },
                                onLikeClick = { id -> viewModel.onLikeClick(id)},
                                isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(mashup.id)
                                    ?: false,
                            )
                        }
                    }
                }
            }
        }
    }
}