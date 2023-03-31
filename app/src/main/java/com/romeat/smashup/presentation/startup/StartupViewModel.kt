package com.romeat.smashup.presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.CookieProvider
import com.romeat.smashup.domain.CheckVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    val cookieProvider: CookieProvider,
    private val checkVersionUseCase: CheckVersionUseCase
) : ViewModel() {

    private val eventChannel = Channel<StartupEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val job = async { checkVersionUseCase.isActualVersion() }

            if (job.await()) { // true means version is ok
                checkCookies()
            } else {
                // show dialog that app is outdated and let user decide what to do
                eventChannel.send(StartupEvent.ShowDialog)
            }
        }
    }

    private suspend fun checkCookies() {
        if(cookieProvider.getCookiesSet().isEmpty()) {
            eventChannel.send(StartupEvent.NavigateToLogin)
        } else {
            eventChannel.send(StartupEvent.NavigateToHome)
        }
    }

    fun onProceedDialogButton() {
        viewModelScope.launch {
            checkCookies()
        }
    }

    fun onExitDialogButton() {

    }
}

sealed class StartupEvent {
    object ShowDialog : StartupEvent()
    object NavigateToLogin : StartupEvent()
    object NavigateToHome : StartupEvent()
}