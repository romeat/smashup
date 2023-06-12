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
import com.romeat.smashup.presentation.home.common.composables.Logo
import com.romeat.smashup.presentation.home.common.composables.PurpleButton
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
            
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = stringResource(id = R.string.welcome),
                textAlign = TextAlign.Center,
                fontStyle = MaterialTheme.typography.body1.fontStyle,
                fontSize = 22.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = stringResource(id = R.string.we_are),
                textAlign = TextAlign.Center,
                fontStyle = MaterialTheme.typography.body1.fontStyle,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(40.dp))
            PurpleButton(
                textRes = R.string.registration,
                onClick = onRegistrationClick
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp),
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colors.onSurface,
                    backgroundColor = MaterialTheme.colors.background,
                ),
                border = null
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
@Preview
fun GreetingsScreenPreview() {
    SmashupTheme(darkTheme = true) {
        GreetingsScreen(
            onRegistrationClick = { },
            onLoginClick = { }
        )
    }
}