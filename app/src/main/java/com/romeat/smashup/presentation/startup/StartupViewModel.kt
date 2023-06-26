package com.romeat.smashup.presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.CheckVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    val loggedUserRepository: LoggedUserRepository,
    private val checkVersionUseCase: CheckVersionUseCase
) : ViewModel() {

    private val eventChannel = Channel<StartupEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val job = async { checkVersionUseCase.isActualVersion() }

            if (job.await()) { // true means version is ok
                checkUserLogged()
            } else {
                // show dialog that app is outdated and let user decide what to do
                eventChannel.send(StartupEvent.ShowDialog)
            }
        }
    }

    private suspend fun checkUserLogged() {
        if(loggedUserRepository.userInfoFlow.value != null) {
            eventChannel.send(StartupEvent.NavigateToHome)
        } else {
            eventChannel.send(StartupEvent.NavigateToLogin)
        }
    }

    fun onProceedDialogButton() {
        viewModelScope.launch {
            checkUserLogged()
        }
    }
}

sealed class StartupEvent {
    object ShowDialog : StartupEvent()
    object NavigateToLogin : StartupEvent()
    object NavigateToHome : StartupEvent()
}