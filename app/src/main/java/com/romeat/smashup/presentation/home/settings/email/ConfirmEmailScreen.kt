package com.romeat.smashup.presentation.home.settings.email

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.common.InfoScreen
import com.romeat.smashup.presentation.common.LoadingScreen

@Composable
fun ConfirmEmailScreen(
    onClose: () -> Unit,
) {
    val viewModel: ConfirmEmailViewModel = hiltViewModel()
    val state = viewModel.state

    if (state.isLoading) {
        LoadingScreen(
            titleRes = R.string.email_change_title,
            textRes = R.string.confirming_new_email,
        )
    } else if (state.isError) {
        InfoScreen(
            topRowTitleResId = R.string.email_change_title,
            iconResId = R.drawable.ic_close_red,
            titleResId = R.string.failed_to_confirm_email,
            subtitle = stringResource(id = viewModel.state.errorResId),
            confirmButtonTextId = R.string.close,
            onConfirmClick = onClose,
            onBackClick = { /* Not used */ },
            showBackButton = false
        )
    } else {
        InfoScreen(
            topRowTitleResId = R.string.email_change_title,
            iconResId = R.drawable.ic_lock,
            titleResId = R.string.email_confirm_success,
            subtitle = stringResource(id = R.string.string_empty),
            confirmButtonTextId = R.string.close,
            onConfirmClick = onClose,
            onBackClick = { /* Not used */ },
            showBackButton = false
        )
    }
}