package com.romeat.smashup.presentation.home.settings.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    toEditPassword: () -> Unit,
    toEditUsername: () -> Unit,
    toEditEmail: () -> Unit,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        ProfileScreenContent(
            state = state,
            onBackClick = onBackClick,
            onEditPasswordClick = toEditPassword,
            onEditUsernameClick = toEditUsername,
            onEditEmailClick = toEditEmail,
            onEditAvatarClick = {}
        )
    }
}

@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onBackClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onEditPasswordClick: () -> Unit,
    onEditUsernameClick: () -> Unit,
    onEditEmailClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopRow(
            title = stringResource(R.string.profile),
            onBackPressed = onBackClick,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { onEditAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                FriendlyGlideImage(
                    imageModel = ImageUrlHelper.authorImageIdToUrl400px(state.imageUrl),
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1.0f)
                        .clip(RoundedCornerShape(30.dp)),
                    error = Placeholder.SmashupDefault.resource,
                )

                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    color = MaterialTheme.colors.surface.copy(alpha = 0.3f)
                ) {}
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "edit avatar"
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            ProfileDataItem(
                titleRes = R.string.displaying_nickname,
                text = state.nickname,
                modifier = Modifier.clickable {
                    onEditUsernameClick()
                },
                showEditIcon = true
            )
            Spacer(modifier = Modifier.height(25.dp))
            ProfileDataItem(
                titleRes = R.string.email_label,
                text = state.email,
                modifier = Modifier.clickable {
                    onEditEmailClick()
                },
                showEditIcon = true
            )
            Spacer(modifier = Modifier.height(25.dp))
            ProfileDataItem(
                titleRes = R.string.password_label,
                text = state.passwordDots,
                modifier = Modifier.clickable {
                    onEditPasswordClick()
                },
                showEditIcon = true
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun ProfileDataItem(
    titleRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    showEditIcon: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.body1
            )

            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        if (showEditIcon) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                contentDescription = "edit"
            )
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun ProfileScreenContentPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = (MaterialTheme.colors.background)
        ) {
            ProfileScreenContent(
                ProfileState(
                    "akkraw",
                    "adwd@dawda.gg",
                    "******"
                ),
                {}, {}, {}, {}, {}
            )
        }
    }
}