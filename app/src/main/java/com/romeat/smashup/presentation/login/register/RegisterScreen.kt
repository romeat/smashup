package com.romeat.smashup.presentation.login.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun RegisterScreen(

) {

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
                onTextChange = onUsernameChange,
                placeholderResId = R.string.string_empty,
                isError = state.isNicknameError,
                modifier = Modifier.fillMaxWidth()
            )
            ErrorText(textRes = state.nicknameErrorResId)

            // Email
            LabelText(
                textRes = R.string.email_label,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = "",
                onTextChange = onEmailChange,
                placeholderResId = R.string.string_empty,
                isError = state.isEmailFormatError,
                modifier = Modifier.fillMaxWidth()
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
                onTextChange = onPasswordChange,
                placeholderResId = R.string.string_empty,
                isError = state.isPasswordError,
                modifier = Modifier.fillMaxWidth()
            )
            ErrorText(textRes = state.passwordErrorResId)

            // General error
            ErrorText(textRes = state.generalErrorResId, emptyLines = 0)

            // Buttons
            PurpleButton(
                textRes = R.string.register_button_2,
                onClick = onRegisterClick
            )
            Spacer(modifier = Modifier.height(20.dp))
            NoBackgroundButton(
                textRes = R.string.login_button_2,
                onClick = onLoginClick
            )
        }
    }
}

@Preview
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
                    nicknameErrorResId = R.string.nick_too_short,
                    isPasswordError = true,
                ),
                {}, {}, {}, {}, {}
            )
        }
    }
}