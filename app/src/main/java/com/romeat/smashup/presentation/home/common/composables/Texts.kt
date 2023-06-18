package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R


@Composable
fun ErrorTextMessage(textResourceId: Int = R.string.failed_to_load) {
    Text(
        text = stringResource(id = textResourceId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextBold22Sp(
    resId: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = stringResource(id = resId),
        textAlign = TextAlign.Center,
        fontStyle = MaterialTheme.typography.body1.fontStyle,
        fontSize = 22.sp,
    )
}

@Composable
fun TextBody1(
    resId: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = stringResource(id = resId),
        textAlign = TextAlign.Center,
        fontStyle = MaterialTheme.typography.body1.fontStyle,
        fontWeight = FontWeight.Normal
    )
}

