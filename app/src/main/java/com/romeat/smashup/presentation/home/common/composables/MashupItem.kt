package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MashupItem(
    mashup: Mashup,
    onBodyClick: (Mashup) -> Unit,
    onInfoClick: (Int) -> Unit,
    isCurrentlyPlaying: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(4.dp)
            .clickable { onBodyClick(mashup) }
    ) {
        GlideImage(
            modifier = Modifier
                .padding(4.dp)
                .size(56.dp),
            contentScale = ContentScale.Crop,
            imageModel = ImageUrlHelper.mashupImageIdToUrl100px(mashup.imageUrl),
            error = ImageVector.vectorResource(id = Placeholder.Napas.resource),
            shimmerParams = ShimmerParams(
                baseColor = MaterialTheme.colors.background,
                highlightColor = MaterialTheme.colors.surface,
                durationMillis = 500,
                tilt = 0f
            )
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .weight(1.0f),
        ) {
            val color = if (isCurrentlyPlaying) {
                MaterialTheme.colors.primaryVariant
            } else {
                Color.Unspecified
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCurrentlyPlaying) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_baseline_play_arrow_24),
                        contentDescription = "",
                        tint = color
                    )
                }
                Text(
                    text = mashup.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mashup.owner,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = color
                )
            }
        }
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .width(48.dp),
            onClick = { onInfoClick(mashup.id) }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector
                    .vectorResource(id = R.drawable.ic_baseline_more_vert_24),
                contentDescription = "more"
            )
        }
    }
}

@Composable
@Preview
fun MashupItemPreview() {
    Column() {
        MashupItem(mashup = Mashup(123, "napas", "The свитер", "", false, 0, 0, 0, listOf(0)), onBodyClick = {}, onInfoClick = {})
        MashupItem(mashup = Mashup(123, "napas", "The свитер", "", false, 0, 0, 0, listOf(0)), onBodyClick = {}, onInfoClick = {}, isCurrentlyPlaying = true)
    }
}