package com.romeat.smashup.presentation.startup

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.R
import com.romeat.smashup.navgraphs.RootGraph
import com.romeat.smashup.presentation.home.common.composables.Logo
import com.romeat.smashup.presentation.home.common.composables.NoBackgroundButton
import com.romeat.smashup.presentation.home.common.composables.PurpleButtonWithProgress
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun StartupScreen(
    navController: NavController,
    viewModel: StartupViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle() { event ->
        when (event) {
            is StartupEvent.NavigateToLogin -> {
                navController.popBackStack()
                navController.navigate(RootGraph.AUTHENTICATION)
            }

            is StartupEvent.NavigateToHome -> {
                navController.popBackStack()
                navController.navigate(RootGraph.HOME)
            }
        }
    }

    StartupScreenContent(
        state = viewModel.state,
        onDownloadUpdateButton = { viewModel.downloadUpdate() },
        onSkipUpdateButton = { viewModel.onSkipUpdateButton() },
        onExitAppButton = { activity?.finishAndRemoveTask() }
    )
}

@Composable
fun StartupScreenContent(
    state: StartupState,
    onDownloadUpdateButton: () -> Unit,
    onSkipUpdateButton: () -> Unit,
    onExitAppButton: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1.0f))
            Logo(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator()
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "v" + BuildConfig.VERSION_NAME)
            }

            if (state.showUpdateDialog) {
                VersionAlertDialog(
                    isOutdated = state.isOutdated,
                    onDownloadUpdateClick = onDownloadUpdateButton,
                    onExitAppClick = onExitAppButton,
                    onSkipUpdateClick = onSkipUpdateButton,
                )
            }
            if (state.showUpdateProgress) {
                UpdateProgressDialog(progress = state.progress)
            }
        }
    }
}

class StartupScreenProvider : PreviewParameterProvider<StartupState> {
    override val values = listOf(
        StartupState(),
        StartupState(showUpdateDialog = true),
        StartupState(showUpdateProgress = true, progress = 0.3f)
    ).asSequence()
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun StartupScreenPreview(
    @PreviewParameter(StartupScreenProvider::class) state: StartupState
) {
    SmashupTheme(darkTheme = true) {
        StartupScreenContent(
            state = state,
            {}, {}, {}
        )
    }
}

@Composable
fun VersionAlertDialog(
    isOutdated: Boolean,
    onDownloadUpdateClick: () -> Unit,
    onSkipUpdateClick: () -> Unit,
    onExitAppClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colors.surface,
                ).padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = if (isOutdated) R.string.version_outdated else R.string.new_version_available),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.height(20.dp))
                PurpleButtonWithProgress(
                    textRes = R.string.button_download_update,
                    onClick = onDownloadUpdateClick,
                )
                Spacer(modifier = Modifier.height(20.dp))
                NoBackgroundButton(
                    textRes = if (isOutdated) R.string.exit_button else R.string.button_skip,
                    onClick = if (isOutdated) onExitAppClick else onSkipUpdateClick,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onSurface,
                        backgroundColor = MaterialTheme.colors.surface,
                        disabledBackgroundColor = MaterialTheme.colors.surface
                    ),
                )
            }
        }
    }
}

@Composable
fun UpdateProgressDialog(
    progress: Float
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colors.surface,
                ).padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.new_version_is_loading),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.height(20.dp))
                LinearProgressIndicator(progress = progress)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}