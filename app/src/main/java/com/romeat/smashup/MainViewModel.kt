package com.romeat.smashup

import androidx.lifecycle.ViewModel
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.presentation.home.settings.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val loggedUserRepository: LoggedUserRepository,
) : ViewModel() {

    val state: StateFlow<LoginResponse?> = loggedUserRepository.userInfoFlow

    fun isUserLogged(): Boolean {
        return loggedUserRepository.userInfoFlow.value == null
    }
}