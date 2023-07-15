package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun UserListCompact(
    users: List<UserProfile>,
    onClick: (Int) -> Unit,
    onMoreClick: () -> Unit = { },
    maxNumberOfItemsToDisplay: Int = 4,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.authors_title),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            if (users.size > maxNumberOfItemsToDisplay) {
                Text(
                    modifier = Modifier.clickable {
                        onMoreClick()
                    },
                    text = stringResource(id = R.string.all),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 15.dp),
        ) {
            items(minOf(users.size, maxNumberOfItemsToDisplay)) { i ->
                UserItem(user = users[i], onClick = { onClick(users[i].id) })
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun UserListCompactPreview() {
    SmashupTheme() {
        Surface(color = MaterialTheme.colors.surface) {
            Column {
                UserListCompact(users = listOf(
                    UserProfile(1, "Chilish", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Mem", "def", 0, listOf(), listOf()),
                ), onClick = {})
                UserListCompact(users = listOf(
                    UserProfile(1, "Chilish", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Mem", "def", 0, listOf(), listOf()),
                    UserProfile(1, "sobakin", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Аркадий", "def", 0, listOf(), listOf()),
                ), onClick = {})
                UserListCompact(users = listOf(
                    UserProfile(1, "Chilish", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Mem", "def", 0, listOf(), listOf()),
                    UserProfile(1, "sobakin", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Аркадий", "def", 0, listOf(), listOf()),
                    UserProfile(1, "Not disp", "def", 0, listOf(), listOf()),
                ), onClick = {})
            }
        }
    }
}