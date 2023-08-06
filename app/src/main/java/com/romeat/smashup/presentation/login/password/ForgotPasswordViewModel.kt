package com.romeat.smashup.presentation.login.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.auth.RequestPasswordRecovery
import com.romeat.smashup.util.LoginFlow.MinUsernameLength
import com.romeat.smashup.util.LoginFlow.UsernameRegex
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val requestPasswordRecovery: RequestPasswordRecovery
) : ViewModel() {

    var state by mutableStateOf(ForgotPasswordState())

    private val eventChannel = Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onEmailChange(value: String) {
        state = state.copy(emailOrNickname = value).andClearErrors()
    }

    fun onSendClick() {
        state = state.copy(
            isLoading = true,
            inputsEnabled = false,
            sendButtonEnabled = false,
        )
        if (isInputValid()) {
            sendRequest()
        } else {
            state = state.copy(
                isLoading = false,
                inputsEnabled = true,
            )
        }
    }

    private fun ForgotPasswordState.andClearErrors() = this.copy(
        isEmailFormatError = false,
        generalErrorResId = R.string.string_empty,
        sendButtonEnabled = true,
    )

    private fun sendRequest() {
        viewModelScope.launch {
            requestPasswordRecovery
                .invoke(state.emailOrNickname)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            eventChannel.send(ForgotPasswordEvent
                                .NavigateToSuccessScreen(state.emailOrNickname))
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                inputsEnabled = true,
                            )
                            state = when(result.code) {
                                404 -> state.copy(generalErrorResId = R.string.error_404_user_not_found)
                                500 -> state.copy(
                                    generalErrorResId = R.string.error_500_server_problems,
                                    sendButtonEnabled = true
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
        // validate email
        if ((!UsernameRegex.matches(state.emailOrNickname) &&
            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.emailOrNickname).matches()) ||
                state.emailOrNickname.length < MinUsernameLength
        ) {
            state = state.copy(
                isEmailFormatError = true,
                generalErrorResId = R.string.wrong_username_or_email_format
            )
            return false
        }
        return true
    }
}

sealed class ForgotPasswordEvent {
    class NavigateToSuccessScreen(val email: String) : ForgotPasswordEvent()
}

data class ForgotPasswordState(
    val emailOrNickname: String = "",
    val isEmailFormatError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val sendButtonEnabled: Boolean = true,
)