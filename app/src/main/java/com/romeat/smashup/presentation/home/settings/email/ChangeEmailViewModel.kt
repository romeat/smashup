package com.romeat.smashup.presentation.home.settings.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.user.profile.ChangeUserEmailUseCase
import com.romeat.smashup.util.LoginFlow
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val changeEmailUseCase: ChangeUserEmailUseCase
) : ViewModel() {
    var state by mutableStateOf(ChangeEmailState())

    fun onCurrentPasswordChange(value: String) {
        state = state.copy(currentPassword = value).andClearErrors()
    }

    fun onNewEmailChange(value: String) {
        state = state.copy(newEmail = value).andClearErrors()
    }

    fun onSendClick() {
        state = state.copy(
            isLoading = true,
            inputEnabled = false,
        )
        if (isInputValid()) {
            changeEmail()
        } else {
            state = state.copy(
                isLoading = false,
                inputEnabled = true,
            )
        }
    }

    private fun changeEmail() {
        viewModelScope.launch {
            changeEmailUseCase
                .invoke(state.currentPassword, state.newEmail)
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

        // validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.newEmail).matches()) {
            state = state.copy(isEmailFormatError = true)
            return false
        }
        return true
    }

    private fun ChangeEmailState.andClearErrors() = this.copy(

        currentPasswordErrorResId = R.string.string_empty,
        isCurrentPasswordError = false,

        isEmailFormatError = false,

        generalErrorResId = R.string.string_empty,
        inputEnabled = true,
        sendButtonEnabled = true,
    )
}

data class ChangeEmailState(
    val currentPassword: String = "",
    val currentPasswordErrorResId: Int = R.string.string_empty,
    val isCurrentPasswordError: Boolean = false,

    val newEmail: String = "",
    val isEmailFormatError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val sendButtonEnabled: Boolean = true,

    val showSuccessScreen: Boolean = false
)