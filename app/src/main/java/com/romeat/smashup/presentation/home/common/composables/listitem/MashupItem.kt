package com.romeat.smashup.presentation.home.common.composables.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.ui.theme.SmashupTheme
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun MashupItem(
    mashup: MashupListItem,
    onBodyClick: (MashupListItem) -> Unit,
    onInfoClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    isCurrentlyPlaying: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(10.dp)
            .clickable { onBodyClick(mashup) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        FriendlyGlideImage(
            imageModel = ImageUrlHelper.mashupImageIdToUrl100px(mashup.imageUrl),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(25)),
            error = Placeholder.SmashupDefault.resource,
        )
        Spacer(modifier = Modifier.size(15.dp))
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
                        modifier = Modifier
                            .size(15.dp)
                            .padding(horizontal = 3.dp),
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_play_standard_button),
                        contentDescription = "",
                        tint = color
                    )
                }
                Text(
                    text = mashup.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
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
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    color = color
                )
            }
        }

        IconButton(
            onClick = { onLikeClick(mashup.id) }
        ) {
            Icon(
                modifier = Modifier
                    .padding(14.dp),
                imageVector = ImageVector
                    .vectorResource(
                        id = if (mashup.isLiked) {
                            R.drawable.ic_heart_filled
                        } else {
                            R.drawable.ic_heart_border
                        }
                    ),
                contentDescription = "more"
            )
        }

        IconButton(
            modifier = Modifier
                .size(48.dp),
            onClick = { onInfoClick(mashup.id) }
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                imageVector = ImageVector
                    .vectorResource(id = R.drawable.ic_more_button),
                contentDescription = "more"
            )
        }
    }
}

@Composable
@Preview
fun MashupItemPreview() {
    SmashupTheme {
        Surface(color = MaterialTheme.colors.surface) {
            Column() {
                MashupItem(
                    mashup = MashupListItem(
                        123,
                        "Лобби под подошвой",
                        "The свитер",
                        "",
                        false,
                        0,
                        0,
                        listOf(0),
                        true
                    ),
                    onBodyClick = {},
                    onInfoClick = {},
                    onLikeClick = {}
                )
                MashupItem(
                    mashup = MashupListItem(
                        123,
                        "napas",
                        "The свитер",
                        "",
                        false,
                        0,
                        0,
                        listOf(0),
                        false
                    ),
                    onBodyClick = {},
                    onInfoClick = {},
                    isCurrentlyPlaying = true,
                    onLikeClick = {}
                )
            }
        }
    }
}