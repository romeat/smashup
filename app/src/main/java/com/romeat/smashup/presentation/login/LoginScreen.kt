package com.romeat.smashup.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.romeat.smashup.R
import com.romeat.smashup.navgraphs.RootGraph
import com.romeat.smashup.util.collectInLaunchedEffectWithLifecycle

@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel: LoginViewModel = hiltViewModel()

    viewModel.eventsFlow.collectInLaunchedEffectWithLifecycle { event ->
        when (event) {
            is LoginEvent.NavigateToHome -> {
                navController.popBackStack()
                navController.navigate(RootGraph.HOME)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LoginScreenContent(
            state = viewModel.state,
            onUsernameChange = { value -> viewModel.onUsernameChange(value) },
            onPasswordChange = { value -> viewModel.onPasswordChange(value) },
            onLoginButtonClick = { viewModel.onLoginButtonClick() }
        )
    }
}

@Composable
fun LoginScreenContent(
    state: LoginFormState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current
    val openDialog = remember { mutableStateOf(false) }
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = {
                onUsernameChange(it)
            },
            isError = state.usernameError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(id = R.string.username_placeholder))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = {
                onPasswordChange(it)
            },
            isError = state.passwordError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(id = R.string.password_placeholder))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (state.loginButtonActive) onLoginButtonClick()
                }
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.ic_baseline_visibility_off_24
                else R.drawable.ic_baseline_visibility_24

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = ImageVector.vectorResource(id = image), "password visibility")
                }
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = state.loginErrorMessageResId),
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                onLoginButtonClick()
            },
            enabled = state.loginButtonActive,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colors.onPrimary,
                backgroundColor = MaterialTheme.colors.primary,
                disabledContentColor = MaterialTheme.colors.onSurface,
                disabledBackgroundColor = MaterialTheme.colors.surface
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp),
                    color = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(
                    text = stringResource(id = R.string.login_button).uppercase(),
                    fontSize = MaterialTheme.typography.button.fontSize,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val signupPart = stringResource(id = R.string.registration)
            val sitePart = stringResource(id = R.string.the_website)

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onSurface
                    )
                ) {
                    pushStringAnnotation(tag = signupPart, annotation = "https://smashup.ru/register")
                    append(signupPart)
                }
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colors.onSurface)
                ) {
                    append(" " + stringResource(id = R.string.and_other_actions) + " ")
                }
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onSurface
                    )
                ) {
                    pushStringAnnotation(tag = sitePart, annotation = "https://smashup.ru")
                    append(sitePart)
                }
            }

            ClickableText(
                text = annotatedString,
                maxLines = 2,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.body2.fontSize
                ),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { span ->
                            uriHandler.openUri(span.item)
                        }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.why_no_registration),
            textDecoration = TextDecoration.Underline,

            fontSize = MaterialTheme.typography.body2.fontSize,
            modifier = Modifier.clickable {
                openDialog.value = true
            }
        )

        if (openDialog.value) {
            InfoDialog(dialogState = openDialog)
        }
    }
}

@Composable
fun InfoDialog(
    dialogState: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        text = {
            Text(
                text = stringResource(id = R.string.no_registration_reasoning),
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
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        dialogState.value = false
                    },
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.onSurface,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.button_understand).uppercase(),
                        fontSize = MaterialTheme.typography.button.fontSize
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun LoginScreenContentPreview() {
    LoginScreenContent(
        state = LoginFormState(password = ""),
        onUsernameChange = { },
        onPasswordChange = { },
        onLoginButtonClick = { }
    )
}
