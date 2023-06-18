package com.romeat.smashup.presentation.login.greetings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.*
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun GreetingsScreen(
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1.0f))
            Logo(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(0.55f))
            TextBold22Sp(
                resId = R.string.welcome,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextBody1(
                resId = R.string.we_are,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            PurpleButton(
                textRes = R.string.registration,
                onClick = onRegistrationClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            NoBackgroundButton(
                textRes = R.string.login_button_1,
                onClick = onLoginClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun GreetingsScreenPreview() {
    SmashupTheme(darkTheme = true) {
        GreetingsScreen(
            onRegistrationClick = { },
            onLoginClick = { }
        )
    }
}