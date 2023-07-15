package com.romeat.smashup.presentation.home.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LanguageOption
import com.romeat.smashup.data.SettingItemOption
import com.romeat.smashup.presentation.home.common.composables.CustomBitrateSlider
import com.romeat.smashup.presentation.home.common.composables.CustomSwitch
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.presentation.home.profile.SettingDescriptionText
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SettingsScreen(
    toProfile: () -> Unit,
    toAboutApp: () -> Unit,
    onBackClick: () -> Unit,
    toAuthScreen: () -> Unit,
) {
    val viewModel: SettingsViewModel = hiltViewModel()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        SettingsScreenContent(
            state = viewModel.state.collectAsState().value,
            onProfileClick = toProfile,
            onBackClick = onBackClick,
            onAboutAppClick = toAboutApp,
            onMultisessionToggle = { /*TODO*/ },
            onExplicitToggle = viewModel::onExplicitToggle,
            onBitrateOption = viewModel::onBitrateOptionSelect,
            onLanguageOption = viewModel::onLanguageOptionSelect,
            onLogoutClick = {
                viewModel.onLogout()
                toAuthScreen()
            },
        )
    }
}

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onProfileClick: () -> Unit,
    onBackClick: () -> Unit,
    onAboutAppClick: () -> Unit,

    onMultisessionToggle: () -> Unit,
    onExplicitToggle: () -> Unit,
    onBitrateOption: (BitrateOption) -> Unit,
    onLanguageOption: (LanguageOption) -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.settings),
            onBackPressed = onBackClick,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                FriendlyGlideImage(
                    imageModel = ImageUrlHelper.authorImageIdToUrl400px(state.imageUrl),
                    modifier = Modifier
                        .size(48.dp)
                        .aspectRatio(1.0f)
                        .clip(RoundedCornerShape(14.dp)),
                    error = Placeholder.Napas.resource,
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.username,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.edit_profile),
                        style = MaterialTheme.typography.body2,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .height(40.dp)
                        .width(24.dp)
                        .rotate(180f),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_button),
                    contentDescription = "to profile"
                )
            }

            // Push notifications
            Spacer(Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.push_notif),
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.height(20.dp))
            SettingItemWithSwitch(
                descriptionRes = R.string.news_notif,
                onToggle = { },
                enabled = false,
                checked = false,
            )
            Spacer(Modifier.height(15.dp))
            SettingItemWithSwitch(
                descriptionRes = R.string.system_notif,
                onToggle = { },
                enabled = false,
                checked = false,
            )

            // Account settings
            Spacer(Modifier.height(25.dp))
            Text(
                text = stringResource(id = R.string.account_settings),
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.height(20.dp))
            SettingItemWithSwitch(
                descriptionRes = R.string.allow_multisessions,
                onToggle = { onMultisessionToggle() },
                checked = state.allowMultisessions,
            )
            Spacer(Modifier.height(15.dp))
            SettingItemWithSwitch(
                descriptionRes = R.string.allow_explicit,
                onToggle = { onExplicitToggle() },
                checked = state.explicitAllowed,
            )
            Spacer(Modifier.height(25.dp))

            // Bitrate
            Text(
                text = stringResource(id = R.string.bitrate),
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.height(20.dp))
            CustomBitrateSlider(
                selected = state.selectedBitrate,
                bitrates = state.bitrateOptions,
                onBitrateChange = onBitrateOption
            )

            // Language
            Spacer(Modifier.height(25.dp))
            Text(
                text = stringResource(id = R.string.other_settings),
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.weight(0.7f),
                    text = stringResource(id = R.string.ui_language)
                )
                SettingWithDropdownMenu<LanguageOption>(
                    modifier = Modifier.weight(0.3f),
                    selectedOption = state.selectedLanguage,
                    allOptions = state.languageOptions,
                    onClick = onLanguageOption
                )
            }

            // About app
            Spacer(Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAboutAppClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.about_app),
                    style = MaterialTheme.typography.h6,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .height(40.dp)
                        .width(24.dp)
                        .rotate(180f),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_button),
                    contentDescription = "about app"
                )
            }

            // Log out
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colors.error,
                    backgroundColor = MaterialTheme.colors.background,
                    disabledBackgroundColor = MaterialTheme.colors.background
                ),
                border = null
            ) {
                Text(
                    text = stringResource(R.string.log_out),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(30.dp))
        }
    }

}

@Composable
fun SettingItemWithSwitch(
    descriptionRes: Int,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = descriptionRes),
            style = MaterialTheme.typography.body1,
        )
        CustomSwitch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onToggle
        )
    }
}

@Composable
fun <T> SettingWithDropdownMenu(
    selectedOption: SettingItemOption,
    allOptions: List<SettingItemOption>,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            text = stringResource(id = selectedOption.displayResId),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded.value = true })
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .defaultMinSize(minWidth = 120.dp)
        ) {
            allOptions.forEachIndexed { index, option ->
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    onClick(option as T)
                }) {
                    Text(text = stringResource(id = option.displayResId))
                }
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun SettingsScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            SettingsScreenContent(
                state = SettingsState(username = "akkraw"),
                onProfileClick = { /*TODO*/ },
                onBackClick = { /*TODO*/ },
                onMultisessionToggle = { /*TODO*/ },
                onExplicitToggle = { /*TODO*/ },
                onBitrateOption = {},
                onLanguageOption = {},
                onAboutAppClick = {},
                onLogoutClick = {},
            )
        }
    }
}