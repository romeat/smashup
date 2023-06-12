package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R

@Composable
fun TopRow(
    onBackPressed: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier

                .width(70.dp)
                .height(50.dp),
            onClick = onBackPressed,
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_button),
                contentDescription = "back"
            )
        }
    }
}