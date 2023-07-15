package com.romeat.smashup.presentation.home.settings.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.NoBackgroundButton
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.presentation.home.common.composables.TopRow
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    toEditPassword: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = (MaterialTheme.colors.background)
    ) {
        ProfileScreenContent(
            state = viewModel.state,
            onBackClick = onBackClick,
            onEditPasswordClick = toEditPassword,
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
                    error = Placeholder.Napas.resource,
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
                text = state.nickname
            )
            Spacer(modifier = Modifier.height(25.dp))
            ProfileDataItem(
                titleRes = R.string.email_label,
                text = state.email
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
                {}, {}, {}
            )
        }
    }
}