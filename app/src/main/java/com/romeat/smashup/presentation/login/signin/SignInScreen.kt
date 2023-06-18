package com.romeat.smashup.presentation.login.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun SignInScreen() {


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
                text = "",
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
                    modifier = Modifier.weight(1f).padding(vertical = 6.dp)
                )
                Text(
                    modifier = Modifier
                        .clickable { onForgotPasswordClick() }
                        .padding(vertical = 6.dp),
                    text = stringResource(R.string.forgot_password),
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colors.primaryVariant
                )
            }
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
                textRes = R.string.login_button_2,
                onClick = onRegisterClick
            )
            Spacer(modifier = Modifier.height(20.dp))
            NoBackgroundButton(
                textRes = R.string.register_button_1,
                onClick = onLoginClick
            )
        }
    }
}

@Preview
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