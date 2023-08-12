package com.romeat.smashup.presentation.home.settings.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.user.profile.ChangeUserPasswordUseCase
import com.romeat.smashup.util.LoginFlow
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changeUserPasswordUseCase: ChangeUserPasswordUseCase
) : ViewModel() {

    var state by mutableStateOf(ChangePasswordState())

    fun onCurrentPasswordChange(value: String) {
        state = state.copy(currentPassword = value).andClearErrors()
    }

    fun onNewPasswordChange(value: String) {
        state = state.copy(newPassword = value).andClearErrors()
    }

    fun onSendClick() {
        state = state.copy(
            isLoading = true,
            inputEnabled = false,
        )
        if (isInputValid()) {
            changePassword()
        } else {
            state = state.copy(
                isLoading = false,
                inputEnabled = true,
            )
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            changeUserPasswordUseCase
                .invoke(state.currentPassword, state.newPassword)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            state = state.copy(
                                showSuccessScreen = true
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = true,
                                inputEnabled = false,
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                inputEnabled = true,
                            )
                            state = when(result.code) {
                                403 -> state.copy(generalErrorResId = R.string.error_403_wrong_current_password)
                                500 -> state.copy(
                                    generalErrorResId = R.string.error_500_server_problems,
                                    sendButtonEnabled = true,
                                )
                                else -> state.copy(
                                    generalErrorResId = R.string.error_general,
                                    sendButtonEnabled = true,
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun isInputValid(): Boolean {
        // validate current password
        if (state.currentPassword.length < LoginFlow.MinPasswordLength) {
            state = state.copy(
                isCurrentPasswordError = true,
                currentPasswordErrorResId = R.string.pass_too_short
            )
            return false
        }

        // validate new password
        if (state.newPassword.length < LoginFlow.MinPasswordLength) {
            state = state.copy(
                isNewPasswordError = true,
                newPasswordErrorResId = R.string.pass_too_short
            )
            return false
        }
        if (!LoginFlow.PasswordRegex.matches(state.newPassword)) {
            state = state.copy(
                isNewPasswordError = true,
                newPasswordErrorResId = R.string.password_wrong_format
            )
            return false
        }
        return true
    }

    private fun ChangePasswordState.andClearErrors() = this.copy(

        currentPasswordErrorResId = R.string.string_empty,
        isCurrentPasswordError = false,

        newPasswordErrorResId = R.string.string_empty,
        isNewPasswordError = false,

        generalErrorResId = R.string.string_empty,
        inputEnabled = true,
        sendButtonEnabled = true,
    )
}

data class ChangePasswordState(
    val currentPassword: String = "",
    val currentPasswordErrorResId: Int = R.string.string_empty,
    val isCurrentPasswordError: Boolean = false,

    val newPassword: String = "",
    val newPasswordErrorResId: Int = R.string.string_empty,
    val isNewPasswordError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val sendButtonEnabled: Boolean = true,

    val showSuccessScreen: Boolean = false
)