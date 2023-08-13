package com.romeat.smashup.presentation.home.settings.username

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.domain.user.profile.ChangeUserNameUseCase
import com.romeat.smashup.util.LoginFlow
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeUsernameViewModel @Inject constructor(
    private val changeUserNameUseCase: ChangeUserNameUseCase
) : ViewModel() {

    var state by mutableStateOf(ChangeUsernameState())

    fun onCurrentPasswordChange(value: String) {
        state = state.copy(currentPassword = value).andClearErrors()
    }

    fun onNewUsernameChange(value: String) {
        state = state.copy(newNickname = value).andClearErrors()
    }

    fun onSendClick() {
        state = state.copy(
            isLoading = true,
            inputEnabled = false,
        )
        if (isInputValid()) {
            changeUsername()
        } else {
            state = state.copy(
                isLoading = false,
                inputEnabled = true,
            )
        }
    }

    private fun changeUsername() {
        viewModelScope.launch {
            changeUserNameUseCase
                .invoke(state.currentPassword, state.newNickname)
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

        // validate nickname
        if (state.newNickname.length < LoginFlow.MinUsernameLength) {
            state = state.copy(
                isNicknameError = true,
                newNicknameErrorResId = R.string.nick_too_short
            )
            return false
        }
        if (!LoginFlow.UsernameRegex.matches(state.newNickname)) {
            state = state.copy(
                isNicknameError = true,
                newNicknameErrorResId = R.string.nick_wrong_format
            )
            return false
        }
        return true
    }

    private fun ChangeUsernameState.andClearErrors() = this.copy(

        currentPasswordErrorResId = R.string.string_empty,
        isCurrentPasswordError = false,

        newNicknameErrorResId = R.string.string_empty,
        isNicknameError = false,

        generalErrorResId = R.string.string_empty,
        inputEnabled = true,
        sendButtonEnabled = true,
    )
}

data class ChangeUsernameState(
    val currentPassword: String = "",
    val currentPasswordErrorResId: Int = R.string.string_empty,
    val isCurrentPasswordError: Boolean = false,

    val newNickname: String = "",
    val newNicknameErrorResId: Int = R.string.string_empty,
    val isNicknameError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,

    val inputEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val sendButtonEnabled: Boolean = true,

    val showSuccessScreen: Boolean = false
)