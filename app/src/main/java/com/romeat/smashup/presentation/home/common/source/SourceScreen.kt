package com.romeat.smashup.presentation.home.common.source

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun SourceScreen(
    onMashupInfoClick: (Int) -> Unit,
    onBackClicked: () -> Unit,
    onExpandPlayerClick: () -> Unit,
    playerViewModel: HomePlayerViewModel,
    navHostController: NavHostController,
    viewModel: SourceViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopRow(
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
                    val info = state.sourceInfo!!
                    LazyColumn() {
                        item {
                            ImageSquare(url = ImageUrlHelper.sourceImageIdToUrl400px(info.imageUrl))
                            ContentDescription(content = info.name)
                            ContentDescription(content = info.owner)
                            Spacer(modifier = Modifier.height(20.dp))
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
                        } else {
                            item {
                                Text(
                                    text = stringResource(id = R.string.mashups_with_source),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            items(
                                items = state.mashupList,
                                key = { it -> it.id }
                            ) { mashup ->
                                MashupItem(
                                    mashup = mashup,
                                    onBodyClick = { viewModel.onMashupClick(it) },
                                    onInfoClick = { id -> onMashupInfoClick(id) },
                                    isCurrentlyPlaying = state.currentlyPlayingMashupId?.equals(
                                        mashup.id
                                    ) ?: false
                                )
                            }
                        }
                    }
                }
            }
            PlayerSmall(
                onExpandClick = onExpandPlayerClick,
                viewModel = playerViewModel
            )
            BottomNavBar(navController = navHostController)
        }
    }
}