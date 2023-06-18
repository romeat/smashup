package com.romeat.smashup.presentation.login.signin

import androidx.lifecycle.ViewModel
import com.romeat.smashup.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

) : ViewModel() {

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