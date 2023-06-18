package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    var hasFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
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
        shape = MaterialTheme.shapes.small,
        value = text,
        onValueChange = onTextChange,
        isError = isError,
        placeholder = {
            Text(
                text = stringResource(id = placeholderResId),
                fontSize = 19.sp,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        },
        textStyle = MaterialTheme.typography.h6,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onSurface),
        singleLine = true,
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
        fontStyle = MaterialTheme.typography.body1.fontStyle,
        fontWeight = FontWeight.W500,
    )
}

@Composable
fun ErrorText(
    textRes: Int,
    modifier: Modifier = Modifier,
    emptyLines: Int = 1,
) {
    Text(
        modifier = modifier,
        text = stringResource(textRes).addEmptyLines(emptyLines),
        fontStyle = MaterialTheme.typography.overline.fontStyle,
        fontWeight = FontWeight.W500,
        color = MaterialTheme.colors.error
    )
}

@Composable
@Preview
fun StyledInputPreview() {
    SmashupTheme() {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

