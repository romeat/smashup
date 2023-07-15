package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.util.SquareDisplayItem
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SquareCard(
    item: SquareDisplayItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(IntrinsicSize.Min)
            .clickable { onClick() }
    ) {
        FriendlyGlideImage(
            imageModel = item.imageUrl,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(15)),
            error = Placeholder.Playlist.resource,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(3.dp))
        if (item.subtitle.isNotBlank()) {
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
fun SquareCardPreview() {
    Row(modifier = Modifier.height(200.dp)) {
        SquareCard(
            SquareDisplayItem(
                id = 1,
                imageUrl = "",
                title = "Лобби под подошвой",
                subtitle = "Утонул в пиве",
            ),
            onClick = {}
        )
        Spacer(modifier = Modifier.size(20.dp))
        SquareCard(
            SquareDisplayItem(
                id = 1,
                imageUrl = "",
                title = "DORADULO",
                subtitle = "Утонул в пиве",
            ),
            onClick = {}
        )
    }
}