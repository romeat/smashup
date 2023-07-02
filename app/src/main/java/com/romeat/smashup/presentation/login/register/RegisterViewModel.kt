package com.romeat.smashup.presentation.login.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import com.romeat.smashup.util.CommonNavigationConstants
import com.romeat.smashup.util.LoginFlow.MaxPasswordLength
import com.romeat.smashup.util.LoginFlow.MaxUsernameLength
import com.romeat.smashup.util.LoginFlow.MinPasswordLength
import com.romeat.smashup.util.LoginFlow.MinUsernameLength
import com.romeat.smashup.util.LoginFlow.PasswordRegex
import com.romeat.smashup.util.LoginFlow.UsernameRegex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val someId: String =
        checkNotNull(savedStateHandle["id"])

    var state by mutableStateOf(RegisterState())

    private val eventChannel = Channel<RegisterEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        Log.d("RegisterViewModel", "RegisterViewModel someId: $someId")
    }

    fun onUsernameChange(value: String) {
        state = state.copy(nickname = value.take(MaxUsernameLength)).andClearErrors()
    }

    fun onEmailChange(value: String) {
        state = state.copy(email = value).andClearErrors()
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value.take(MaxPasswordLength)).andClearErrors()
    }

    fun onRegisterClick() {
        state = state.copy(
            isLoading = true,
            inputsEnabled = false,
            registerButtonEnabled = false,
        )
        if (isInputValid()) {
            // todo useCase call & update state
            register()
        } else {
            state = state.copy(
                isLoading = false,
                inputsEnabled = true,
            )
        }
    }

    private fun RegisterState.andClearErrors() = this.copy(
        nicknameErrorResId = R.string.string_empty,
        isNicknameError = false,

        passwordErrorResId = R.string.string_empty,
        isPasswordError = false,

        isEmailFormatError = false,

        generalErrorResId = R.string.string_empty,
        isGeneralError = false,
        registerButtonEnabled = true,
    )

    private fun register() {

    }

    private fun isInputValid(): Boolean {
        // validate nickname
        if (state.nickname.length < MinUsernameLength) {
            state = state.copy(
                isNicknameError = true,
                nicknameErrorResId = R.string.nick_too_short
            )
            return false
        }
        if (!UsernameRegex.matches(state.nickname)) {
            state = state.copy(
                isNicknameError = true,
                nicknameErrorResId = R.string.nick_wrong_format
            )
            return false
        }

        // validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            state = state.copy(isEmailFormatError = true)
            return false
        }

        // validate password
        if (state.password.length < MinPasswordLength) {
            state = state.copy(
                isPasswordError = true,
                passwordErrorResId = R.string.pass_too_short
            )
            return false
        }
        if (!PasswordRegex.matches(state.password)) {
            state = state.copy(
                isPasswordError = true,
                passwordErrorResId = R.string.password_wrong_format
            )
            return false
        }
        return true
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

    val inputsEnabled: Boolean = true,
    val registerButtonEnabled: Boolean = true,
    val isLoading: Boolean = false,
)
