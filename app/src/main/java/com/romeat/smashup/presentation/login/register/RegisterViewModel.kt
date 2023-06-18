package com.romeat.smashup.presentation.login.register

import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

) : ViewModel() {

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
