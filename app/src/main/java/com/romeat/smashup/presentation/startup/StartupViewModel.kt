package com.romeat.smashup.presentation.startup

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.AppVersionRepository
import com.romeat.smashup.data.SmashupVersion
import com.romeat.smashup.data.VERSION_PREFS_FILE
import com.romeat.smashup.util.getTimeFromPrefs
import com.romeat.smashup.util.updateLastTime
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val loggedUserRepository: LoggedUserRepository,
    private val appVersionRepository: AppVersionRepository,
) : ViewModel() {

    // last known time when update dialog was shown
    private val VERSION_LAST_SHOWN_DIALOG = "LastShownDialog"

    // min period between showing dialog - 4 days
    private val newVersionDialogThreshold = if (BuildConfig.DEBUG) TimeUnit.SECONDS.toMillis(30)
        else TimeUnit.DAYS.toMillis(4)

    var state by mutableStateOf(StartupState())

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
        val result = appVersionRepository.getVersion()
        if (scope.isActive) {
            viewModelScope.launch {
                val currentTime = System.currentTimeMillis()
                val lastShownDialogTime = context.getTimeFromPrefs(VERSION_LAST_SHOWN_DIALOG, VERSION_PREFS_FILE)
                when (result) {
                    SmashupVersion.Latest -> checkUserLogged()
                    else -> {
                        if (lastShownDialogTime - currentTime < newVersionDialogThreshold) {
                            state = state.copy(showUpdateDialog = true, isOutdated = result is SmashupVersion.Outdated)
                            context.updateLastTime(
                                name = VERSION_LAST_SHOWN_DIALOG,
                                prefsFile = VERSION_PREFS_FILE,
                                timestamp = currentTime,
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun checkUserLogged() {
        if (loggedUserRepository.getCurrentUser() != null) {
            eventChannel.send(StartupEvent.NavigateToHome)
        } else {
            eventChannel.send(StartupEvent.NavigateToLogin)
        }
    }

    fun onSkipUpdateButton() {
        state = state.copy(showUpdateDialog = false)
        viewModelScope.launch {
            checkUserLogged()
        }
    }

    fun downloadUpdate() {
        state = state.copy(showUpdateDialog = false, showUpdateProgress = true)
        viewModelScope.launch {
            val deferred = async (Dispatchers.IO) {
                appVersionRepository.downloadNewVersion()
            }
            appVersionRepository.progressFlow.collect {
                state = state.copy(progress = it)
            }
        }
    }
}

data class StartupState(
    val isOutdated: Boolean = false,
    val showUpdateDialog: Boolean = false,
    val showUpdateProgress: Boolean = false,
    val progress: Float = 0f,
)

sealed class StartupEvent {
    object NavigateToLogin : StartupEvent()
    object NavigateToHome : StartupEvent()
}