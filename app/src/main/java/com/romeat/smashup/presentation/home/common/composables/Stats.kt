package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.util.toStringWithThousands

@Composable
fun StatsRow(
    likes: Int,
    listens: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = likes.toStringWithThousands(),
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.width(5.dp)
        )
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector
                .vectorResource(id = R.drawable.ic_baseline_favorite_24),
            contentDescription = "likes"
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(25.dp),
            color = MaterialTheme.colors.onSurface
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = listens.toStringWithThousands(),
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.width(5.dp)
        )
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector
                .vectorResource(id = R.drawable.ic_baseline_headphones_24),
            contentDescription = "listens"
        )
    }
}

@Preview
@Composable
fun StatsPreview() {
    Column() {
        StatsRow(likes = 77, listens = 123)
        StatsRow(likes = 7, listens = 23)
        StatsRow(likes = 1277, listens = 11123)
    }
}
