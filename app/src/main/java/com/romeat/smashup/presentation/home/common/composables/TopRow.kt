package com.romeat.smashup.presentation.home.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romeat.smashup.R
import com.romeat.smashup.ui.theme.SmashupTheme

@Composable
fun TopRow(
    title: String,
    onBackPressed: () -> Unit,
    showBackButton: Boolean = true,
) {
    val height = 50.dp
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            if (showBackButton) {
                IconButton(
                    modifier = Modifier
                        .width(70.dp)
                        .height(height),
                    onClick = onBackPressed,
                ) {
                    Icon(
                        modifier = Modifier
                            .height(40.dp)
                            .width(24.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_button),
                        contentDescription = "back"
                    )
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun TransparentTopRow(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState
) {
    val canScrollBack = remember { derivedStateOf { scrollState.canScrollBackward } }

    val height = 50.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (canScrollBack.value) MaterialTheme.colors.surface.copy(alpha = 0.5f)
                    else Color.Transparent)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            IconButton(
                modifier = Modifier
                    .width(70.dp)
                    .height(height),
                onClick = onBackPressed,
            ) {
                Icon(
                    modifier = Modifier
                        .height(40.dp)
                        .width(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_button),
                    contentDescription = "back"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun TopRowPreview() {
    SmashupTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopRow("Настройки", {})

                TopRow("", {})

                TopRow("Настройки", {}, false)
            }
        }
    }
}