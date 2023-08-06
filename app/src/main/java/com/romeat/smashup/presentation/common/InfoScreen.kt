package com.romeat.smashup.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.TextBold22Sp
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun InfoScreen(
    topRowTitleResId: Int,
    iconResId: Int,
    titleResId: Int,
    subtitle: String,
    confirmButtonTextId: Int,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    showBackButton: Boolean = true,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            TopRow(
                title = stringResource(topRowTitleResId),
                onBackPressed = onBackClick,
                showBackButton = showBackButton
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier.size(64.dp),
                    imageVector = ImageVector
                        .vectorResource(id = iconResId),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(25.dp))
                TextBold22Sp(
                    resId = titleResId,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = subtitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = onConfirmClick,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onSurface,
                        backgroundColor = MaterialTheme.colors.surface,
                        disabledBackgroundColor = MaterialTheme.colors.surface
                    ),
                    border = null
                ) {
                    Text(
                        text = stringResource(confirmButtonTextId),
                        style = MaterialTheme.typography.h6,
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun InfoScreenContent() {
    SmashupTheme() {
        InfoScreen(
            topRowTitleResId = R.string.forgot_password_label,
            iconResId = R.drawable.ic_stars,
            titleResId = R.string.we_are,
            subtitle = "Мы отправили инструкции по смене пароля на вашу почту 3dwarka@gmail.com",
            onConfirmClick = { /*TODO*/ },
            confirmButtonTextId = R.string.exit_button,
            onBackClick = { /*TODO*/ },
        )
    }
}