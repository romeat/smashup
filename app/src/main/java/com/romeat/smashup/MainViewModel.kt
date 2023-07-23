package com.romeat.smashup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.dto.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val loggedUserRepository: LoggedUserRepository,
) : ViewModel() {

    private var lastKnownUserId: Int? = null

    private val _eventChannel = Channel<MainEvent>(Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    val state: StateFlow<LoginResponse?> = loggedUserRepository.userInfoFlow

    init {
        viewModelScope.launch {
            loggedUserRepository.userInfoFlow.collect { user ->
                if (user == null && lastKnownUserId != null) {
                    _eventChannel.send(MainEvent.NavigateToAuth)
                } else if (user != null) {
                    lastKnownUserId = user.id
                }
            }
        }
    }

    fun isUserLogged(): Boolean {
        return loggedUserRepository.userInfoFlow.value != null
    }
}

sealed class MainEvent {
    object NavigateToAuth : MainEvent()
}