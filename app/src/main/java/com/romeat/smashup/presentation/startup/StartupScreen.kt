package com.romeat.smashup.presentation.startup

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.R
import com.romeat.smashup.navgraphs.RootGraph
import com.romeat.smashup.ui.theme.AppGreenColor
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun StartupScreen(
    navController: NavController,
    viewModel: StartupViewModel = hiltViewModel()
) {

    val openDialog = remember { mutableStateOf(false) }

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

            is StartupEvent.ShowDialog ->
                openDialog.value = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1.0f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator()
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    text = stringResource(id = R.string.loading),
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )
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

            if (openDialog.value) {
                VersionAlertDialog(
                    dialogState = openDialog,
                    onProceedAnywayClick = { viewModel.onProceedDialogButton() },
                    onExitAppClick = { viewModel.onExitDialogButton() }
                )
            }
        }
    }
}

@Composable
fun VersionAlertDialog(
    dialogState: MutableState<Boolean>,
    onProceedAnywayClick: () -> Unit,
    onExitAppClick: () -> Unit
) {
    val activity = (LocalContext.current as? Activity)

    AlertDialog(
        onDismissRequest = { },
        text = {
            Text(
                text = stringResource(id = R.string.version_outdated),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(10.dp),
                    onClick = {
                        dialogState.value = false
                        onProceedAnywayClick()
                    },
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppGreenColor,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.proceed_anyway_button).uppercase(),
                        fontSize = MaterialTheme.typography.button.fontSize
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(10.dp),
                    onClick = {
                        dialogState.value = false
                        activity?.finishAndRemoveTask()
                    },
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_button).uppercase(),
                        fontSize = MaterialTheme.typography.button.fontSize
                    )
                }
            }
        }
    )
}