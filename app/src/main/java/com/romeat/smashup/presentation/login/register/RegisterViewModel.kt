package com.romeat.smashup.presentation.login.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

) : ViewModel() {

    var state by mutableStateOf(RegisterState())

    private val eventChannel = Channel<RegisterEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onUsernameChange(value: String) {

    }

    fun onEmailChange(value: String) {

    }

    fun onPasswordChange(value: String) {

    }

    fun onRegisterClick() {

    }
}

sealed class RegisterEvent {
    class NavigateToRegistrationConfirm(val email: String) : RegisterEvent()
}

data class RegisterState(
    val nickname: String = "",
    val nicknameErrorResId: Int = R.string.string_empty,
    val isNicknameError: Boolean = false,

    val password: String = "",
    val passwordErrorResId: Int = R.string.string_empty,
    val isPasswordError: Boolean = false,

    val email: String = "",
    val isEmailFormatError: Boolean = false,

    val isGeneralError: Boolean = false,
    val generalErrorResId: Int = R.string.string_empty,

    val isLoading: Boolean = false,
)
