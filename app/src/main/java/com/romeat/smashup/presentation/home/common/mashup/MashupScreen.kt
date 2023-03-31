package com.romeat.smashup.presentation.home.common.mashup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.R

@Composable
fun MashupScreen(
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    viewModel: MashupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopRow(
            onBackPressed = onBackClicked
        )
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
                    Divider(
                        modifier = Modifier.padding(
                            horizontal = 10.dp
                        )
                    )
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
                            modifier = Modifier.padding(horizontal = 12.dp)
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
}