package com.romeat.smashup.presentation.home.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.HomePlayerViewModel
import com.romeat.smashup.presentation.home.common.composables.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    onPlaylistClick: (Int) -> Unit,
    onExpandPlayerClick: () -> Unit,
    navHostController: NavHostController,
    playerViewModel: HomePlayerViewModel,
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.charts),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.h4.fontSize,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
            ) {
                if (state.isLoading) {
                    CustomCircularProgressIndicator()
                } else if (state.isError) {
                    ErrorTextMessage()
                } else {
                    PlaylistsGrid(
                        playlists = state.playlists,
                        onPlaylistClick = { id -> onPlaylistClick(id) }
                    )
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