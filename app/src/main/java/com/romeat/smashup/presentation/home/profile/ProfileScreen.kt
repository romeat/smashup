package com.romeat.smashup.presentation.home.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LanguageOption
import com.romeat.smashup.data.SettingItemOption
import com.romeat.smashup.presentation.home.common.composables.CustomCircularProgressIndicator
import com.romeat.smashup.presentation.home.common.composables.ErrorTextMessage
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.ui.theme.AppGreenColor
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else {
                ProfileScreenContent(
                    onLogoutClick = {
                        viewModel.onLogout()
                        onLogoutClick()
                    },
                    state = state,
                    onBitrateOption = {
                        viewModel.onBitrateOptionSelect(it)
                    },
                    onExplicitToggle = {
                        viewModel.onExplicitToggle()
                    },
                    onLanguageOption = {
                        viewModel.onLanguageOptionSelect(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileScreenContent(
    onLogoutClick: () -> Unit,
    state: ProfileScreenState,
    onBitrateOption: (BitrateOption) -> Unit,
    onLanguageOption: (LanguageOption) -> Unit,
    onExplicitToggle: () -> Unit
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
                    FriendlyGlideImage(
                        imageModel = ImageUrlHelper.authorImageIdToUrl400px(state.imageUrl),
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1.0f)
                            .clip(CircleShape),
                        error = Placeholder.Napas.resource,
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

        SettingItem(description = stringResource(id = R.string.ui_language)) {
            SettingWithDropdownMenu<LanguageOption>(
                selectedOption = state.selectedLanguage,
                allOptions = state.languageOptions,
                onClick = onLanguageOption
            )
        }

        SettingItem(description = stringResource(id = R.string.bitrate)) {
            SettingWithDropdownMenu<BitrateOption>(
                selectedOption = state.selectedBitrate,
                allOptions = state.bitrateOptions,
                onClick = onBitrateOption
            )
        }

        SettingItem(description = stringResource(id = R.string.explicit_content)) {
            Switch(
                checked = state.explicitAllowed,
                onCheckedChange = {
                    onExplicitToggle()
                }
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

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
fun <T> SettingWithDropdownMenu(
    selectedOption: SettingItemOption,
    allOptions: List<SettingItemOption>,
    onClick: (T) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            text = stringResource(id = selectedOption.displayResId),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded.value = true })
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .defaultMinSize(minWidth = 120.dp)
        ) {
            allOptions.forEachIndexed { index, option ->
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    onClick(option as T)
                }) {
                    Text(text = stringResource(id = option.displayResId))
                }
            }
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
                    onClick = { dialogState.value = false },
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
    ProfileScreenContent(
        onLogoutClick = { /*TODO*/ },
        state = ProfileScreenState(isLoading = false, username = "Asdod"),
        onBitrateOption = { },
        onExplicitToggle = { },
        onLanguageOption = { }
    )
}