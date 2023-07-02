package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PurpleButtonWithProgress(
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    inProgress: Boolean = false
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            disabledContentColor = MaterialTheme.colors.surface,
            disabledBackgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        if (inProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(30.dp),
                color = MaterialTheme.colors.surface
            )
        } else {
            Text(
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun NoBackgroundButton(
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.background,
            disabledBackgroundColor = MaterialTheme.colors.background
        ),
        border = null
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.h6,
        )
    }
}