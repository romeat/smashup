package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClickableDescription(
    name: String,
    onNameClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            maxLines = 2,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.h5.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { onNameClick(name) }
        )
    }
}

@Preview
@Composable
fun ClickableDescriptionPreview() {
    ClickableDescription(name = "MOVIESTRAIFE", onNameClick = {})
}

@Preview
@Composable
fun ClickableDescriptionPreviewLong() {
    ClickableDescription(name = "MOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFE", onNameClick = {})
}

@Preview
@Composable
fun ClickableDescriptionPreviewReallyLong() {
    ClickableDescription(name = "MOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFEMOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFE", onNameClick = {})
}