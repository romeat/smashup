package com.romeat.smashup.presentation.login.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.auth.ConfirmPasswordRecovery
import com.romeat.smashup.util.LoginFlow.MinPasswordLength
import com.romeat.smashup.util.LoginFlow.PasswordRegex
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetNewPasswordViewModel @Inject constructor(
    private val confirmPasswordRecovery: ConfirmPasswordRecovery,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val token: String =
        checkNotNull(savedStateHandle["id"])

    var state by mutableStateOf(NewPasswordState())

    private val eventChannel = Channel<NewPasswordEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onPasswordChange(value: String) {
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
            confirmPasswordRecovery
                .invoke(token, state.newPassword)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            eventChannel.send(NewPasswordEvent.NavigateToSuccessScreen)
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
                                404 -> state.copy(generalErrorResId = R.string.error_404_request_unknown)
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

    private fun NewPasswordState.andClearErrors() = this.copy(
        passwordErrorResId = R.string.string_empty,
        isPasswordError = false,

        generalErrorResId = R.string.string_empty,
        inputEnabled = true,
        sendButtonEnabled = true,
    )

    private fun isInputValid(): Boolean {
        // validate password
        if (state.newPassword.length < MinPasswordLength) {
            state = state.copy(
                isPasswordError = true,
                passwordErrorResId = R.string.pass_too_short
            )
            return false
        }
        if (!PasswordRegex.matches(state.newPassword)) {
            state = state.copy(
                isPasswordError = true,
                passwordErrorResId = R.string.password_wrong_format
            )
            return false
        }
        return true
    }
}

sealed class NewPasswordEvent {
    object NavigateToSuccessScreen : NewPasswordEvent()
}

data class NewPasswordState(
    val newPassword: String = "",
    val passwordErrorResId: Int = R.string.string_empty,
    val isPasswordError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val sendButtonEnabled: Boolean = true,
)