package com.romeat.smashup.presentation.login.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    toSuccessScreen: (String) -> Unit
) {
    val viewModel: ForgotPasswordViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is ForgotPasswordEvent.NavigateToSuccessScreen -> {
                toSuccessScreen(event.email)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        ForgotPasswordScreenContent(
            state = viewModel.state,
            onEmailChange = viewModel::onEmailChange,
            onSendClick = viewModel::onSendClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun ForgotPasswordScreenContent(
    state: ForgotPasswordState,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.forgot_password_label),
            onBackPressed = onBackClick,
            showBackButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            TextBold22Sp(resId = R.string.password_recovery)
            Spacer(modifier = Modifier.height(10.dp))
            TextBody1(resId = R.string.password_recovery_hint)

            Spacer(modifier = Modifier.weight(0.1f))

            // Email
            LabelText(
                textRes = R.string.login_email_or_nick,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = state.emailOrNickname,
                enabled = state.inputsEnabled,
                onTextChange = onEmailChange,
                placeholderResId = R.string.string_empty,
                isError = state.isEmailFormatError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (!state.isLoading) onSendClick()
                    }
                ),
            )

            ErrorText(textRes = state.generalErrorResId)

            // Buttons
            PurpleButtonWithProgress(
                textRes = R.string.send_button,
                onClick = onSendClick,
                enabled = state.sendButtonEnabled,
                inProgress = state.isLoading,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun ForgotPasswordScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            ForgotPasswordScreenContent(
                state = ForgotPasswordState(),
                {}, {}, {}
            )
        }
    }
}