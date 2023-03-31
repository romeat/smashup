package com.romeat.smashup.presentation.home.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.CustomCircularProgressIndicator
import com.romeat.smashup.presentation.home.common.composables.ErrorTextMessage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.ui.theme.AppGreenColor
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state

    if (state.isLoading) {
        CustomCircularProgressIndicator()
    } else {
        ProfileScreenContent(
            onLogoutClick = {
                viewModel.onLogout()
                onLogoutClick()
            },
            state = state
        )
    }
}

@Composable
fun ProfileScreenContent(
    onLogoutClick: () -> Unit,
    state: ProfileScreenState
) {
    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isError) {
            ErrorTextMessage(textResourceId = R.string.failed_to_load_user_profile)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlideImage(
                        imageModel = ImageUrlHelper.authorImageIdToUrl400px(state.imageUrl),
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1.0f)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        error = ImageVector.vectorResource(id = Placeholder.Napas.resource),
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = MaterialTheme.colors.surface,
                            durationMillis = 700,
                            tilt = 0f
                        )
                    )
                    Text(
                        text = state.username,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.body1.fontSize
                    )
                }
            }
        }

        SettingItem(description = stringResource(id = R.string.bitrate)) {
            Text(text = "128 кб/с")
        }

        SettingItem(description = stringResource(id = R.string.ui_language)) {
            Text(text = "RUS")
        }

        SettingItem(description = stringResource(id = R.string.explicit_content)) {
            Text(text = "ON")
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = { openDialog.value = true },
            border = null,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.error,
                backgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = stringResource(id = R.string.log_out).uppercase(),
                fontSize = MaterialTheme.typography.button.fontSize
            )
        }

        if (openDialog.value) {
            ConfirmationDialog(
                dialogState = openDialog,
                onConfirmClick = {
                    onLogoutClick()
                }
            )
        }
    }
}

@Composable
fun SettingItem(
    description: String,
    content: @Composable() () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(0.7f)) {
            SettingDescriptionText(text = description)
        }
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.End
        ) {
            content()
        }
    }
}

@Composable
fun SettingDescriptionText(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}

@Composable
fun ConfirmationDialog(
    dialogState: MutableState<Boolean>,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        text = {
            Text(
                text = stringResource(id = R.string.log_out_confirmation),
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
                        .padding(10.dp),
                    onClick = {
                        dialogState.value = false
                        onConfirmClick()
                    },
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppGreenColor,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.button_yes).uppercase(),
                        fontSize = MaterialTheme.typography.button.fontSize
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = { dialogState.value = false  },
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.error,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.button_no).uppercase(),
                        fontSize = MaterialTheme.typography.button.fontSize
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun ProfileScreenContentPreview() {
    ProfileScreenContent(onLogoutClick = { /*TODO*/ }, state = ProfileScreenState(isLoading = false, username = "Asdod"))
}