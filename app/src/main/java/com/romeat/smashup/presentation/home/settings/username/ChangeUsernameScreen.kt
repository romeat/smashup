package com.romeat.smashup.presentation.home.settings.username

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.common.InfoScreen
import com.romeat.smashup.presentation.home.common.composables.ErrorText
import com.romeat.smashup.presentation.home.common.composables.LabelText
import com.romeat.smashup.presentation.home.common.composables.PurpleButtonWithProgress
import com.romeat.smashup.presentation.home.common.composables.StyledInput
import com.romeat.smashup.presentation.home.common.composables.StyledPasswordInput
import com.romeat.smashup.presentation.home.common.composables.TextBody1
import com.romeat.smashup.presentation.home.common.composables.TopRow

@Composable
fun ChangeUsernameScreen(
    onBackClick: () -> Unit,
    onSuccessClick: () -> Unit,
) {
    val viewModel: ChangeUsernameViewModel = hiltViewModel()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        if (viewModel.state.showSuccessScreen) {
            InfoScreen(
                topRowTitleResId = R.string.username_change_title,
                onBackClick = onBackClick,
                onConfirmClick = onSuccessClick,
                titleResId = R.string.confirm_new_username,
                subtitle = stringResource(R.string.confirmation_mail),
                iconResId = R.drawable.ic_mail_stack,
                confirmButtonTextId = R.string.confirm
            )
        } else {
            ChangeUsernameScreenContent(
                state = viewModel.state,
                onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
                onNewUsernameChange = viewModel::onNewUsernameChange,
                onSendClick = viewModel::onSendClick,
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
fun ChangeUsernameScreenContent(
    state: ChangeUsernameState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewUsernameChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.username_change_title),
            onBackPressed = onBackClick,
            showBackButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            TextBody1(resId = R.string.username_change_info)

            Spacer(modifier = Modifier.weight(0.1f))

            // Current password
            LabelText(
                textRes = R.string.current_password,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledPasswordInput(
                password = state.currentPassword,
                enabled = state.inputEnabled,
                isError = state.isCurrentPasswordError,
                onPasswordChange = onCurrentPasswordChange,
                focusManager = focusManager
            )
            ErrorText(textRes = state.currentPasswordErrorResId)

            // New nickname
            LabelText(
                textRes = R.string.new_username,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = state.newNickname,
                enabled = state.inputEnabled,
                onTextChange = onNewUsernameChange,
                placeholderResId = R.string.nickname_hint,
                isError = state.isNicknameError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                )
            )
            ErrorText(textRes = state.newNicknameErrorResId)

            ErrorText(textRes = state.generalErrorResId)
            // Buttons
            PurpleButtonWithProgress(
                textRes = R.string.button_change,
                onClick = onSendClick,
                enabled = state.sendButtonEnabled,
                inProgress = state.isLoading,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}