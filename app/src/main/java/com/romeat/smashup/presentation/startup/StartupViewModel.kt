package com.romeat.smashup.presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.CheckVersionUseCase
import com.romeat.smashup.domain.SmashupVersion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
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
            val checkVersionJob = async(Dispatchers.IO) { checkVersion(this) }
            delay(4500)
            // cancel if it takes too long
            if (checkVersionJob.isActive) {
                checkVersionJob.cancel()
                checkUserLogged()
            }
        }
    }

    private fun checkVersion(scope: CoroutineScope) {
        val result = checkVersionUseCase.getVersion()
        if (scope.isActive) {
            viewModelScope.launch {
                when (result) {
                    SmashupVersion.Actual,
                    SmashupVersion.Latest -> checkUserLogged()

                    SmashupVersion.Outdated -> eventChannel.send(StartupEvent.ShowDialog)
                }
            }
        }
    }

    private suspend fun checkUserLogged() {
        if (loggedUserRepository.userInfoFlow.value != null) {
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