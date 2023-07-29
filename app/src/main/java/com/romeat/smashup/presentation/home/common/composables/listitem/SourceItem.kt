package com.romeat.smashup.presentation.home.common.composables.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.presentation.home.common.composables.FriendlyGlideImage
import com.romeat.smashup.presentation.home.common.composables.Placeholder
import com.romeat.smashup.util.ImageUrlHelper

@Composable
fun SourceItem(
    source: Source,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .clickable { onClick(source.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        FriendlyGlideImage(
            imageModel = ImageUrlHelper.sourceImageIdToUrl100px(source.imageUrl),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            error = Placeholder.SmashupDefault.resource,
        )
        Spacer(modifier = Modifier.size(15.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .weight(1.0f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = source.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    fontWeight = FontWeight.Bold,
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
                    text = source.owner,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                )
            }
        }

    }
}

@Composable
@Preview
fun SourceItemPreview() {
    SourceItem(source = Source(123, "napas", listOf("The свитер"), ""), onClick = {})
}