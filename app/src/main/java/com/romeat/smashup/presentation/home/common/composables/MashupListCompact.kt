package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem

@Composable
fun MashupListCompact(
    mashups: List<MashupListItem>,
    onMashupClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onMashupInfoClick: (Int) -> Unit,
    onMoreClick: () -> Unit = { },
    currentlyPlayingMashupId: Int? = null,
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
                text = stringResource(id = R.string.mashups_title),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            if (mashups.size > maxNumberOfItemsToDisplay) {
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
            contentPadding = PaddingValues(vertical = 10.dp),
        ) {
            items(minOf(mashups.size, maxNumberOfItemsToDisplay)) { i ->
                val mashup = mashups[i]
                MashupItem(
                    mashup = mashup,
                    onBodyClick = { onMashupClick(it.id) },
                    onInfoClick = { id -> onMashupInfoClick(id) },
                    onLikeClick = { id -> onLikeClick(id)},
                    isCurrentlyPlaying = currentlyPlayingMashupId?.equals(mashup.id)
                        ?: false
                )
            }
        }
    }
}