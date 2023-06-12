package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R

@Composable
fun PurpleButton(
    textRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp),
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
        Text(
            text = stringResource(id = textRes),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}