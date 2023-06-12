package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R

@Composable
fun Logo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo),
            contentDescription = "logo"
        )
        Text(
            text = "SmashUp",
            fontSize = 62.sp
        )
    }
}