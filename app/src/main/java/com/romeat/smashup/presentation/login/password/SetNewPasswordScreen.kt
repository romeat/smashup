package com.romeat.smashup.presentation.login.password

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun SetNewPasswordScreen(
    onBackClick: () -> Unit,
    toSuccessScreen: () -> Unit
) {
    val viewModel: SetNewPasswordViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is NewPasswordEvent.NavigateToSuccessScreen -> {
                toSuccessScreen()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        SetNewPasswordScreenContent(
            state = viewModel.state,
            onPasswordChange = viewModel::onPasswordChange,
            onSendClick = viewModel::onSendClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun SetNewPasswordScreenContent(
    state: NewPasswordState,
    onPasswordChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

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

            TextBold22Sp(resId = R.string.new_password_creation)
            Spacer(modifier = Modifier.height(10.dp))
            TextBody1(resId = R.string.type_new_password)

            Spacer(modifier = Modifier.weight(0.1f))

            // Password
            LabelText(
                textRes = R.string.password_label,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            StyledInput(
                text = state.newPassword,
                enabled = state.inputEnabled,
                onTextChange = onPasswordChange,
                placeholderResId = R.string.password_hint,
                isError = state.isPasswordError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (!state.isLoading) onSendClick()
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

            ErrorText(textRes = state.generalErrorResId)

            // Buttons
            PurpleButtonWithProgress(
                textRes = R.string.confirm,
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
fun SetNewPasswordScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            SetNewPasswordScreenContent(
                state = NewPasswordState(),
                {}, {}, {}
            )
        }
    }
}