package com.romeat.smashup.presentation.home.settings.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.Logo
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.about_app),
            onBackPressed = onBackClick,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Logo()
            Text(
                text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Normal,
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextTitleWithLink(
                    titleRes = R.string.privacy_policy,
                    onClick = { uriHandler.openUri("https://smashup.ru/privacy") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextTitleWithLink(
                    titleRes = R.string.user_agreement,
                    onClick = { uriHandler.openUri("https://smashup.ru/rules") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextTitleWithLink(
                    titleRes = R.string.external_components,
                    onClick = { uriHandler.openUri("https://github.com/romeat/smashup") } // todo change
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.our_team),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.team_nicknames),
                    style = MaterialTheme.typography.body2,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.adult_content_warning),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.touch_point),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}

@Composable
fun TextTitleWithLink(
    titleRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_link),
            contentDescription = "link"
        )
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun AboutScreenPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            AboutScreen {
                {}
            }
        }
    }
}