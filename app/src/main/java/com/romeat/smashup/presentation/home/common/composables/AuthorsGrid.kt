package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.musicservice.mapper.MediaMetadataMapper

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