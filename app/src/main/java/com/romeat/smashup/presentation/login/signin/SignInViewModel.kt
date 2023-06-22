package com.romeat.smashup.presentation.login.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.LoginUseCase
import com.romeat.smashup.util.LoginFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loggedUser: LoggedUserRepository
) : ViewModel() {

    var state by mutableStateOf(SignInState())

    private val eventChannel = Channel<SignInEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onUsernameChange(value: String) {
        state = state.copy(nickname = value.take(LoginFlow.MaxUsernameLength)).andClearErrors()
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value.take(LoginFlow.MaxPasswordLength)).andClearErrors()
    }

    fun onLoginClick() {
        state = state.copy(
            isLoading = true,
            inputsEnabled = false,
            loginButtonEnabled = false,
        )
        if (isInputValid()) {
            login()
        } else {
            state = state.copy(
                isLoading = false,
                inputsEnabled = true,
            )
        }
    }

    private fun login() {
        // todo
    }

    private fun isInputValid(): Boolean {
        // validate nickname
        if (state.nickname.length < LoginFlow.MinUsernameLength) {
            state = state.copy(
                isNicknameError = true,
                nicknameErrorResId = R.string.nick_too_short
            )
            return false
        }

        // validate password
        if (state.password.length < LoginFlow.MinPasswordLength) {
            state = state.copy(
                isPasswordError = true,
                passwordErrorResId = R.string.pass_too_short
            )
            return false
        }
        return true
    }

    private fun SignInState.andClearErrors() = this.copy(
        nicknameErrorResId = R.string.string_empty,
        isNicknameError = false,

        passwordErrorResId = R.string.string_empty,
        isPasswordError = false,

        generalErrorResId = R.string.string_empty,
        loginButtonEnabled = true,
    )
}

sealed class SignInEvent {
    object NavigateToHomeGraph : SignInEvent()
}

data class SignInState(
    val nickname: String = "",
    val nicknameErrorResId: Int = R.string.string_empty,
    val isNicknameError: Boolean = false,

    val password: String = "",
    val passwordErrorResId: Int = R.string.string_empty,
    val isPasswordError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val loginButtonEnabled: Boolean = true,
)