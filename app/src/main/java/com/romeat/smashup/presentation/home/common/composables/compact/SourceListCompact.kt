package com.romeat.smashup.presentation.home.common.composables.compact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.presentation.home.common.composables.listitem.SourceItem
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun SourceListCompact(
    sources: List<Source>,
    onClick: (Int) -> Unit,
    onMoreClick: () -> Unit = { },
    maxNumberOfItemsToDisplay: Int = 3,
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
                text = stringResource(id = R.string.sources_title),
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            if (sources.size > maxNumberOfItemsToDisplay) {
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
            sources.take(minOf(sources.size, maxNumberOfItemsToDisplay)).forEach { source ->
                SourceItem(source = source, onClick = { onClick(source.id) })
            }
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
fun SourceListCompactPreview() {
    SmashupTheme() {
        Surface(color = MaterialTheme.colors.surface) {
            Column {
                SourceListCompact(sources = listOf(
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                ), onClick = {})
                SourceListCompact(sources = listOf(
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                ), onClick = {})
                SourceListCompact(sources = listOf(
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                    Source(1, "Дора", listOf("Дура"), "def"),
                    Source(1, "Lobby mod", listOf("Yopta", "Chel", "Bruh"), "def"),
                ), onClick = {})
            }
        }
    }
}