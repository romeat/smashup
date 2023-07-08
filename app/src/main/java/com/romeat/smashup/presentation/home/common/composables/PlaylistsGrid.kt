package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.data.dto.Playlist
import com.romeat.smashup.util.ImageUrlHelper
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

// BIG TODO - remove everything
// todo remove - used on search screen
@Composable
fun PlaylistsGrid(
    playlists: List<Playlist>,
    onPlaylistClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
    ) {
        items(playlists.size) { i ->
            val playlist = playlists[i]
            PlaylistPreview(
                imageUrl = ImageUrlHelper.playlistImageIdToUrl400px(playlist.imageUrl),
                title = playlist.name,
                owner = playlist.owner,
                onClick = { onPlaylistClick(playlist.id) }
            )
        }
    }
}

@Composable
fun PlaylistPreview(
    imageUrl: String,
    title: String,
    owner: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() }
    ) {
        GlideImage(
            imageModel = imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f),
            contentScale = ContentScale.Crop,
            error = ImageVector.vectorResource(id = Placeholder.Playlist.resource),
            shimmerParams = ShimmerParams(
                baseColor = MaterialTheme.colors.background,
                highlightColor = MaterialTheme.colors.surface,
                durationMillis = 700,
                tilt = 0f
            )
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = owner,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AuthorPreview(
    imageUrl: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() }
    ) {
        GlideImage(
            imageModel = ImageUrlHelper.authorImageIdToUrl400px(imageUrl),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = ImageVector.vectorResource(id = Placeholder.Napas.resource),
            shimmerParams = ShimmerParams(
                baseColor = MaterialTheme.colors.background,
                highlightColor = MaterialTheme.colors.surface,
                durationMillis = 700,
                tilt = 0f
            )
        )
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AuthorsGrid(
    authors: List<AuthorProfile>,
    onAuthorClick: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
    ) {
        items(authors.size) { i ->
            val author = authors[i]
            AuthorPreview(
                imageUrl = author.imageUrl,
                title = author.username,
                onClick = { onAuthorClick(author.username) }
            )
        }
    }
}


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

@Preview
@Composable
fun ContentDescriptionPreview() {
    ContentDescription(content = "Playlist")
    ContentDescription(content = "Playlist ContentDescription PreviewLong aaaaaaaaaaaaa")
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

@Preview
@Composable
fun ClickableDescriptionPreview() {
    ClickableDescription(name = "MOVIESTRAIFE", onNameClick = {})
}

@Preview
@Composable
fun ClickableDescriptionPreviewLong() {
    ClickableDescription(name = "MOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFE", onNameClick = {})
}

@Preview
@Composable
fun ClickableDescriptionPreviewReallyLong() {
    ClickableDescription(name = "MOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFEMOVIESTRAIFE MOVIESTRAIFE MOVIESTRAIFE", onNameClick = {})
}