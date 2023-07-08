package com.romeat.smashup.presentation.login.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.auth.RegisterUseCase
import com.romeat.smashup.util.LoginFlow.MaxPasswordLength
import com.romeat.smashup.util.LoginFlow.MaxUsernameLength
import com.romeat.smashup.util.LoginFlow.MinPasswordLength
import com.romeat.smashup.util.LoginFlow.MinUsernameLength
import com.romeat.smashup.util.LoginFlow.PasswordRegex
import com.romeat.smashup.util.LoginFlow.UsernameRegex
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    var state by mutableStateOf(RegisterState())

    private val eventChannel = Channel<RegisterEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onUsernameChange(value: String) {
        state = state.copy(nickname = value.take(MaxUsernameLength)).andClearErrors()
    }

    fun onEmailChange(value: String) {
        state = state.copy(email = value.trim()).andClearErrors()
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
        viewModelScope.launch {
            registerUseCase
                .invoke(state.nickname, state.email, state.password)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            eventChannel.send(RegisterEvent.NavigateToSuccessScreen(state.email))
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                inputsEnabled = true,
                                generalErrorResId = result.message?.toResIdError()
                                    ?: R.string.error_general,
                                registerButtonEnabled = true,
                            )
                        }
                    }
                }
        }
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

    private fun String.toResIdError(): Int {
        return when (this) {
            "register.bad_password",
            "register.bad_email",
            "register.bad_username" -> R.string.error_400_bad_register_parameters

            "register.email_exists",
            "register.username_exists" -> R.string.error_400_user_exists

            "register.already_sent" -> R.string.error_400_email_already_sent

            else -> R.string.error_500_server_problems
        }
    }
}

sealed class RegisterEvent {
    class NavigateToSuccessScreen(val email: String) : RegisterEvent()
}

data class RegisterState(
    val nickname: String = "romeat",
    val nicknameErrorResId: Int = R.string.string_empty,
    val isNicknameError: Boolean = false,

    val password: String = "Ktcyfz69",
    val passwordErrorResId: Int = R.string.string_empty,
    val isPasswordError: Boolean = false,

    val email: String = "romeat@yandex.ru",
    val isEmailFormatError: Boolean = false,

    val isGeneralError: Boolean = false,
    val generalErrorResId: Int = R.string.string_empty,

    val inputsEnabled: Boolean = true,
    val registerButtonEnabled: Boolean = true,
    val isLoading: Boolean = false,
)
