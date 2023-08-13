package com.romeat.smashup.presentation.home.settings.password

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.romeat.smashup.R
import com.romeat.smashup.presentation.common.InfoScreen
import com.romeat.smashup.presentation.common.LoadingScreen

@Composable
fun ConfirmPasswordScreen(
    onClose: () -> Unit,
) {
    val viewModel: ConfirmPasswordViewModel = hiltViewModel()
    val state = viewModel.state

    if (state.isLoading) {
        LoadingScreen(
            titleRes = R.string.password_change_title,
            textRes = R.string.confirming_new_password,
        )
    } else if (state.isError) {
        InfoScreen(
            topRowTitleResId = R.string.password_change_title,
            iconResId = R.drawable.ic_close_red,
            titleResId = R.string.failed_to_confirm_password,
            subtitle = stringResource(id = viewModel.state.errorResId),
            confirmButtonTextId = R.string.close,
            onConfirmClick = onClose,
            onBackClick = { /* Not used */ },
            showBackButton = false
        )
    } else {
        InfoScreen(
            topRowTitleResId = R.string.password_change_title,
            iconResId = R.drawable.ic_lock,
            titleResId = R.string.password_updated_succesfully,
            subtitle = stringResource(id = R.string.password_updated_info),
            confirmButtonTextId = R.string.close,
            onConfirmClick = onClose,
            onBackClick = { /* Not used */ },
            showBackButton = false
        )
    }
}