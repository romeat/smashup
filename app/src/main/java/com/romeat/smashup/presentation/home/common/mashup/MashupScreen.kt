package com.romeat.smashup.presentation.home.common.mashup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel

@Composable
fun MashupScreen(
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    onExpandPlayerClick: () -> Unit,
    playerViewModel: HomePlayerViewModel,
    navHostController: NavHostController,
    viewModel: MashupViewModel = hiltViewModel(),
) {
    val state = viewModel.state

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
                    val info = state.mashupInfo!!
                    LazyColumn() {
                        item {
                            ImageSquare(url = ImageUrlHelper.mashupImageIdToUrl400px(info.imageUrl))
                            ContentDescription(content = info.name)
                            ClickableDescription(
                                name = info.owner,
                                onNameClick = { onAuthorClick(info.owner) })
                            StatsRow(likes = info.likes, listens = info.streams)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        if (state.isSourceListLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                ) {
                                    CustomCircularProgressIndicator()
                                }
                            }
                        } else if (state.isSourceListError) {
                            item {
                                ListLoadingError()
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(id = R.string.sources_of_mashup),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            items(
                                items = state.sourceList,
                                key = { it -> it.id }
                            ) { source ->
                                SourceItem(
                                    source = source,
                                    onClick = { id -> onSourceClick(id) }
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