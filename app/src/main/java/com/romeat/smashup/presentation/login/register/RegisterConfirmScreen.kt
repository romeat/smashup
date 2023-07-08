package com.romeat.smashup.presentation.login.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.common.InfoScreen
import com.romeat.smashup.presentation.home.common.composables.TextBody1
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle


@Composable
fun RegisterConfirmScreen(
    onCloseClick: () -> Unit,
    toHomeGraph: () -> Unit,
) {
    val viewModel: RegisterConfirmViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is RegisterConfirmEvent.NavigateToHomeGraph -> {
                toHomeGraph()
            }
        }
    }
    if (viewModel.state.isLoading) {
        LoadingScreen()
    } else {
        InfoScreen(
            topRowTitleResId = R.string.register_title,
            iconResId = R.drawable.ic_close_red,
            titleResId = R.string.registration_confirm_error,
            subtitle = stringResource(id = R.string.error_500_server_problems),//,viewmodel.state.errorResId),
            confirmButtonTextId = R.string.close,
            onConfirmClick = onCloseClick,
            onBackClick = { /* Not used */ },
            showBackButton = false
        )
    }
}

@Composable
fun LoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            TopRow(
                title = stringResource(R.string.register_title),
                onBackPressed = { },
                showBackButton = false
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator()
                }
                TextBody1(
                    resId = R.string.confirming_registration,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun RegisterConfirmScreenPreview() {
    SmashupTheme {
        LoadingScreen()
    }
}