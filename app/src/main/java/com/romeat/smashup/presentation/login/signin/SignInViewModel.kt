package com.romeat.smashup.presentation.login.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.LoginUseCase
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

    }

    fun onPasswordChange(value: String) {

    }

    fun onLoginClick() {

    }
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
    val isLoading: Boolean = false,
)