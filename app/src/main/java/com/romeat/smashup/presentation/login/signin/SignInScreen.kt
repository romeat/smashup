package com.romeat.smashup.presentation.login.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
fun SignInScreen(
    toRegister: () -> Unit,
    toHomeScreen: () -> Unit,
    toForgotPassword: () -> Unit
) {
    val viewModel: SignInViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is SignInEvent.NavigateToHomeGraph -> {
                toHomeScreen()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        SignInScreenContent(
            state = viewModel.state,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRegisterClick = toRegister,
            onLoginClick = viewModel::onLoginClick,
            onForgotPasswordClick = toForgotPassword,
        )
    }
}

@Composable
fun SignInScreenContent(
    state: SignInState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.login),
            onBackPressed = { },
            showBackButton = false
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Username
            LabelText(
                textRes = R.string.login_email_or_nick,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = state.nickname,
                enabled = !state.isLoading,
                onTextChange = onUsernameChange,
                placeholderResId = R.string.string_empty,
                isError = state.isNicknameError,
                modifier = Modifier.fillMaxWidth()
            )
            ErrorText(textRes = state.nicknameErrorResId)

            // Password
            Row(modifier = Modifier.fillMaxWidth()) {
                LabelText(
                    textRes = R.string.password_label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp)
                )
                Text(
                    modifier = Modifier
                        .clickable { if (!state.isLoading) onForgotPasswordClick() }
                        .padding(vertical = 6.dp),
                    text = stringResource(R.string.forgot_password),
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colors.primaryVariant
                )
            }
            StyledInput(
                text = state.password,
                enabled = !state.isLoading,
                onTextChange = onPasswordChange,
                placeholderResId = R.string.string_empty,
                isError = state.isPasswordError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (!state.isLoading) onLoginClick()
                    }
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.ic_baseline_visibility_off_24
                    else R.drawable.ic_baseline_visibility_24

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = image),
                            "password visibility"
                        )
                    }
                }
            )
            ErrorText(textRes = state.passwordErrorResId)

            // General error
            ErrorText(textRes = state.generalErrorResId, emptyLines = 0)

            // Buttons
            PurpleButtonWithProgress(
                textRes = R.string.login_button_2,
                onClick = {
                    focusManager.clearFocus()
                    onLoginClick()
                },
                enabled = !state.isLoading,
                inProgress = state.isLoading
            )
            Spacer(modifier = Modifier.height(20.dp))
            NoBackgroundButton(
                textRes = R.string.register_button_1,
                onClick = onRegisterClick,
                enabled = !state.isLoading,
            )
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun SignInScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            SignInScreenContent(
                SignInState(),
                {}, {}, {}, {}, {}
            )
        }
    }
}