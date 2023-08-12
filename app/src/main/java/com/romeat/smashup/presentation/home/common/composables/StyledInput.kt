package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.addEmptyLines

@Composable
fun StyledInput(
    text: String,
    onTextChange: (String) -> Unit,
    placeholderResId: Int,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    trailingIcon: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onSurface,
        disabledTextColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    )
) {
    var hasFocus by remember { mutableStateOf(false) }

    @OptIn(ExperimentalMaterialApi::class)
    BasicTextField(
        value = text,
        modifier = modifier
            .height(48.dp)
            .border(
                width = 2.dp,
                color = if (hasFocus)
                    MaterialTheme.colors.primaryVariant
                else if (isError)
                    MaterialTheme.colors.error
                else
                    MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            )
            .onFocusChanged { focusState -> hasFocus = focusState.hasFocus }
            .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.small),
        onValueChange = onTextChange,
        enabled = enabled,
        textStyle = MaterialTheme.typography.h6.merge(TextStyle(color = colors.textColor(enabled).value)),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = true,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = text,
                visualTransformation = visualTransformation,
                placeholder = {
                    Text(
                        text = stringResource(id = placeholderResId),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                },
                contentPadding = PaddingValues(horizontal = 15.dp),
                innerTextField = innerTextField,
                trailingIcon = trailingIcon,
                singleLine = true,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape = MaterialTheme.shapes.small
                    )
                }
            )
        }
    )
}

@Composable
fun StyledPasswordInput(
    password: String,
    enabled: Boolean,
    onPasswordChange: (String) -> Unit,
    isError: Boolean,
    focusManager: FocusManager,
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    StyledInput(
        text = password,
        enabled = enabled,
        onTextChange = onPasswordChange,
        placeholderResId = R.string.password_hint,
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
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
}


@Composable
fun LabelText(
    textRes: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = stringResource(textRes),
        style = MaterialTheme.typography.body1,
    )
}

@Composable
fun ErrorText(
    textRes: Int,
    modifier: Modifier = Modifier,
    emptyLines: Int = 1,
) {
    Text(
        modifier = modifier.padding(vertical = 3.dp),
        text = stringResource(textRes).addEmptyLines(emptyLines),
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.W500,
        color = MaterialTheme.colors.error,
        maxLines = 2
    )
}

@Composable
@Preview
fun StyledInputPreview() {
    SmashupTheme() {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                Spacer(modifier = Modifier.size(20.dp))

                StyledInput(
                    text = "akkraw",
                    onTextChange = {},
                    placeholderResId = R.string.username_placeholder,
                    isError = false,
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))

                StyledInput(
                    text = "akkraw",
                    onTextChange = {},
                    placeholderResId = R.string.username_placeholder,
                    isError = false,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))

                StyledInput(
                    text = "",
                    onTextChange = {},
                    placeholderResId = R.string.username_placeholder,
                    isError = false,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))

                StyledInput(
                    text = "",
                    onTextChange = {},
                    placeholderResId = R.string.username_placeholder,
                    isError = false,
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.size(20.dp))

                StyledInput(
                    text = "",
                    onTextChange = {},
                    placeholderResId = R.string.username_placeholder,
                    isError = true,
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }

        }
    }
}

