package com.romeat.smashup.presentation.login.password

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
class ForgotPasswordViewModel @Inject constructor(

) : ViewModel() {

    var state by mutableStateOf(ForgotPasswordState())

    private val eventChannel = Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onEmailChange(value: String) {

    }

    fun onSendClick() {

    }
}

sealed class ForgotPasswordEvent {
    class NavigateToSuccessScreen(val email: String) : ForgotPasswordEvent()
}

data class ForgotPasswordState(
    val email: String = "",
    val isEmailFormatError: Boolean = false,

    val generalErrorResId: Int = R.string.string_empty,
    val isLoading: Boolean = false,
)