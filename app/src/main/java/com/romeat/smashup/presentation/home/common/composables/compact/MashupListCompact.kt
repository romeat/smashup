package com.romeat.smashup.presentation.home.common.composables.compact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.presentation.home.common.composables.listitem.MashupItem

@Composable
fun MashupListCompact(
    mashups: List<MashupListItem>,
    onMashupClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onMashupInfoClick: (String) -> Unit,
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
                    modifier = Modifier
                        .widthIn(min = 48.dp)
                        .clickable { onMoreClick() },
                    text = stringResource(id = R.string.all),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.End,
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        ) {
            mashups.take(minOf(mashups.size, maxNumberOfItemsToDisplay)).forEach { mashup ->
                MashupItem(
                    mashup = mashup,
                    onBodyClick = { onMashupClick(it.id) },
                    onInfoClick = { onMashupInfoClick(mashup.serializedMashup) },
                    onLikeClick = { id -> onLikeClick(id)},
                    isCurrentlyPlaying = currentlyPlayingMashupId?.equals(mashup.id)
                        ?: false
                )
            }
        }
    }
}