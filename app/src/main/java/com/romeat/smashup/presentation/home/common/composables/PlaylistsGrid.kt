package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.ImageUrlHelper
import com.romeat.smashup.util.toStringWithThousands
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

// todo remove
@Composable
fun ContentDescription(
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = content,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.h5.fontSize,
            textAlign = TextAlign.Center
        )
    }
}



// todo remove
@Composable
fun ClickableDescription(
    name: String,
    onNameClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //.padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            maxLines = 2,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.h5.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { onNameClick(name) }
        )
    }
}


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
                .vectorResource(id = R.drawable.ic_heart_border),
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
