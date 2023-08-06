package com.romeat.smashup.presentation.login.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.romeat.smashup.R
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.auth.ConfirmRegisterUseCase
import com.romeat.smashup.presentation.login.signin.SignInEvent
import com.romeat.smashup.util.Constants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterConfirmViewModel @Inject constructor(
    private val loggedUserRepository: LoggedUserRepository,
    private val savedStateHandle: SavedStateHandle,
    private val confirmRegisterUseCase: ConfirmRegisterUseCase,
) : ViewModel() {

    private val token: String =
        checkNotNull(savedStateHandle["id"])

    var state by mutableStateOf(RegisterConfirmState())

    private val eventChannel = Channel<RegisterConfirmEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            confirmRegisterUseCase
                .invoke(token)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            loggedUserRepository.updateUserStat(result.data!!)
                            eventChannel.send(
                                RegisterConfirmEvent.NavigateToHomeGraph
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            state = when (result.code) {
                                404 -> state.copy(
                                    isLoading = false,
                                    errorResId = R.string.error_404_request_unknown
                                )
                                500 -> state.copy(
                                    isLoading = false,
                                    errorResId = R.string.error_500_registartion_server_error
                                )
                                else -> state.copy(
                                    isLoading = false,
                                    errorResId = R.string.error_general
                                )
                            }
                        }
                    }
                }
        }
    }
}

sealed class RegisterConfirmEvent {
    object NavigateToHomeGraph : RegisterConfirmEvent()
}

data class RegisterConfirmState(
    val isLoading: Boolean = true,
    val errorResId: Int = R.string.string_empty,
)