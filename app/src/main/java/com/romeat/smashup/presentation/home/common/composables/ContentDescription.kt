package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ContentDescription(
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = content,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.h5.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ContentDescriptionPreview() {
    ContentDescription(content = "Playlist")
    ContentDescription(content = "Playlist ContentDescription PreviewLong aaaaaaaaaaaaa")
}
