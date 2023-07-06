package com.romeat.smashup.presentation.login.password

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.romeat.smashup.R
import com.romeat.smashup.presentation.common.InfoScreen
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun EmailRecoverySentScreen(
    email: String,
    onConfirmClick: () -> Unit,
) {
    InfoScreen(
        topRowTitleResId = R.string.forgot_password_label,
        iconResId = R.drawable.ic_mail_stack,
        titleResId = R.string.instructions_sent,
        subtitle = stringResource(R.string.instructions_sent_details, email),
        confirmButtonTextId = R.string.confirm,
        onConfirmClick = onConfirmClick,
        onBackClick = { /* Not used */ },
        showBackButton = false
    )
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun EmailRecoverySentPreview() {
    SmashupTheme {
        EmailRecoverySentScreen(
            "3dwarka@gmail.com"
        ) { }
    }
}


@Composable
fun PasswordUpdatedScreen(
    onConfirmClick: () -> Unit,
) {
    InfoScreen(
        topRowTitleResId = R.string.forgot_password_label,
        iconResId = R.drawable.ic_stars,
        titleResId = R.string.password_updated_succesfully,
        subtitle = stringResource(id = R.string.password_updated_info),
        confirmButtonTextId = R.string.button_yay,
        onConfirmClick = onConfirmClick,
        onBackClick = { /* Not used */ },
        showBackButton = false
    )
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun PasswordUpdatedScreenPreview() {
    SmashupTheme {
        PasswordUpdatedScreen { }
    }
}