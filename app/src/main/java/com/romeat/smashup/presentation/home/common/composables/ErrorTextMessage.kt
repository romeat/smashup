package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R

@Composable
fun ErrorTextMessage(textResourceId: Int = R.string.failed_to_load) {
    Text(
        text = stringResource(id = textResourceId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        textAlign = TextAlign.Center
    )
}