package com.romeat.smashup.presentation.home.common.mashup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.presentation.home.common.composables.listitem.SourceItem
import com.romeat.smashup.presentation.home.common.composables.listitem.UserItem
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun MashupScreen(
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (Int) -> Unit,
    viewModel: MashupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        MashupScreenContent(
            state = state,
            onSourceClick = onSourceClick,
            onAuthorClick = onAuthorClick,
            onLikeClick = viewModel::onLikeClick
        )
    }
}

@Composable
fun MashupScreenContent(
    state: MashupScreenState,
    onSourceClick: (Int) -> Unit,
    onAuthorClick: (Int) -> Unit,
    onLikeClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isProgress) {
            CustomCircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(modifier = Modifier.size(70.dp)) }
                item {
                    FriendlyGlideImage(
                        imageModel = ImageUrlHelper.mashupImageIdToUrl800px(state.mashupInfo.imageUrl),
                        modifier = Modifier.size(280.dp),
                        error = Placeholder.SmashupDefault.resource
                    )
                }
                item { Spacer(modifier = Modifier.size(20.dp)) }
                item {
                    Text(
                        text = state.mashupInfo.name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
                item { Spacer(modifier = Modifier.size(45.dp)) }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clickable { onLikeClick() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(6.dp),
                            imageVector = if (state.isLiked) {
                                ImageVector.vectorResource(R.drawable.ic_heart_filled)
                            } else {
                                ImageVector.vectorResource(R.drawable.ic_heart_border)
                            },
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(
                                if (state.isLiked)
                                    R.string.remove_from_favourite
                                else
                                    R.string.add_to_favourite
                            ),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                item { Spacer(modifier = Modifier.size(20.dp)) }

                // SOURCES
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(6.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_album_24),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.mashup_sources),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                if (state.isSourceListError) {
                    item {
                        Text(
                            text = stringResource(R.string.failed_to_load),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    items(state.sourceList.size) { i ->
                        SourceItem(
                            source = state.sourceList[i],
                            onClick = onSourceClick,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.size(20.dp)) }

                // AUTHORS
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_person_24),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.mashup_authors),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                if (state.isAuthorListError) {
                    item {
                        Text(
                            text = stringResource(R.string.failed_to_load),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    items(
                        items = state.authorList,
                        key = { it.id }
                    ) { author ->
                        UserItem(
                            user = author,
                            onClick = onAuthorClick,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Preview
@Composable
fun MashupScreenContentPreview() {
    SmashupTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            MashupScreenContent(
                state = MashupScreenState(
                    mashupInfo = Mashup(
                        12,
                        "DOraduso ghop",
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        "def", 1,1,1,1,2, 3
                    ),
                    isLiked = true,
                    isSourceListLoading = false,
                    isAuthorListLoading = false,
                    sourceList = listOf(
                        Source(1, "Дора", listOf("Дура"), "def"),
                        Source(2, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                        Source(3, "Дора", listOf("Дура"), "def"),
                        Source(4, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                    ),
                    authorList = listOf(
                        UserProfile(1, "Chilish", "def", 0, 0, listOf(), listOf()),
                        UserProfile(3, "sobakin", "def", 0, 0, listOf(), listOf()),
                        UserProfile(4, "Аркадий", "def", 0, 0, listOf(), listOf()),
                    ),
                ),{}, {}, {}
            )
        }
    }
}