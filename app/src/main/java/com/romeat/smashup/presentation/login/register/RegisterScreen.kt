package com.romeat.smashup.presentation.login.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun RegisterScreen(
    navController: NavController,
    onLoginClick: () -> Unit
) {
    val viewModel: RegisterViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is RegisterEvent.NavigateToRegistrationConfirm -> {
                navController.popBackStack()
                // todo navigate to
            }
        }
    }

    RegisterScreenContent(
        state = viewModel.state,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onEmailChange = viewModel::onEmailChange,
        onRegisterClick = viewModel::onRegisterClick,
        onLoginClick = onLoginClick
    )
}

@Composable
fun RegisterScreenContent(
    state: RegisterState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.register_title),
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
                textRes = R.string.register_nickname_label,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = "",
                enabled = !state.isLoading,
                onTextChange = onUsernameChange,
                placeholderResId = R.string.string_empty,
                isError = state.isNicknameError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            ErrorText(textRes = state.nicknameErrorResId)

            // Email
            LabelText(
                textRes = R.string.email_label,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = "",
                enabled = !state.isLoading,
                onTextChange = onEmailChange,
                placeholderResId = R.string.string_empty,
                isError = state.isEmailFormatError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            ErrorText(
                textRes = if (state.isEmailFormatError) R.string.email_format_error else R.string.string_empty
            )

            // Password
            LabelText(
                textRes = R.string.password_label,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = "",
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
                        if (!state.isLoading) onRegisterClick()
                    }
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.ic_baseline_visibility_off_24
                    else R.drawable.ic_baseline_visibility_24

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = ImageVector.vectorResource(id = image), "password visibility")
                    }
                }
            )
            ErrorText(textRes = state.passwordErrorResId)

            // General error
            ErrorText(textRes = state.generalErrorResId, emptyLines = 0)

            // Buttons
            PurpleButton(
                textRes = R.string.register_button_2,
                onClick = onRegisterClick,
                enabled = !state.isLoading,
            )
            Spacer(modifier = Modifier.height(20.dp))
            NoBackgroundButton(
                textRes = R.string.login_button_2,
                onClick = onLoginClick,
                enabled = !state.isLoading,
            )
        }
    }
}

@Preview(locale = "ru")
@Preview(locale = "en")
@Composable
fun RegisterScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            RegisterScreenContent(
                state = RegisterState(
                    isNicknameError = true,
                    nicknameErrorResId = R.string.nick_wrong_format,
                    isPasswordError = true,
                    passwordErrorResId = R.string.password_wrong_format
                ),
                {}, {}, {}, {}, {}
            )
        }
    }
}
