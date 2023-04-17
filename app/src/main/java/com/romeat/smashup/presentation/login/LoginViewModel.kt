package com.romeat.smashup.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.data.CookieProvider
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.GetUserInfoUseCase
import com.romeat.smashup.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val cookieProvider: CookieProvider,
    private val loginUseCase: LoginUseCase,
    private val dataUseCase: GetUserInfoUseCase,
    private val loggedUser: LoggedUserRepository
) : ViewModel() {

    var state by mutableStateOf(LoginFormState())

    private val eventChannel = Channel<LoginEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onUsernameChange(value: String) {
        state = state.copy(
            username = value,
            usernameError = !validateUsernameMinLength(value),
            loginButtonActive = validateFieldsMinLength(email = value),
            loginErrorMessageResId = R.string.string_empty
        )
    }

    fun onPasswordChange(value: String) {
        state = state.copy(
            password = value,
            passwordError = !validatePasswordMinLength(value),
            loginButtonActive = validateFieldsMinLength(password = value),
            loginErrorMessageResId = R.string.string_empty
        )
    }

    fun onLoginButtonClick() {
        state = state.copy(
            loginButtonActive = false,
            isLoading = true,
            loginErrorMessageResId = R.string.string_empty
        )
        login()
    }

    private fun validateFieldsMinLength(
        email: String = state.username,
        password: String = state.password
    ) : Boolean {
        return validateUsernameMinLength(email) && validatePasswordMinLength(password)
    }

    private fun validateUsernameMinLength(value: String) : Boolean {
        return value.length >= 4
    }

    private fun validatePasswordMinLength(value: String) : Boolean {
        return value.length >= 8
    }

    private fun login() {
        viewModelScope.launch {
            try {
                val response = loginUseCase.invoke(state.username, state.password)
                if (response.isSuccessful) {
                    // no cookies = wrong login/password
                    if (!loggedUser.isUserLogged()) {
                        state = state.copy(
                            loginButtonActive = false,
                            isLoading = false,
                            loginErrorMessageResId = R.string.wrong_username_or_password
                        )
                    } else { // successfully logged in
                        loggedUser.setName(state.username)
                        eventChannel.send(LoginEvent.NavigateToHome)
                    }
                } else {  // server always returns 200 OK, so this block "is" unreachable
                    val error = HttpException(response).message()
                    state = state.copy(
                        loginButtonActive = true,
                        isLoading = false,
                        loginErrorMessageResId = R.string.wrong_username_or_password
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    loginButtonActive = true,
                    isLoading = false,
                    loginErrorMessageResId = R.string.something_went_wrong_try_later
                )
            }
        }
    }

}

sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
}

data class LoginFormState(
    val username: String = "",
    val usernameError: Boolean = false,
    val password: String = "",
    val passwordError: Boolean = false,
    val loginErrorMessageResId: Int = R.string.string_empty,
    val isLoading: Boolean = false,
    val loginButtonActive: Boolean = false
)