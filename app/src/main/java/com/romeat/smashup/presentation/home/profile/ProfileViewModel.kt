package com.romeat.smashup.presentation.home.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.CookieProvider
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.GetUserInfoUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.MediaConstants
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val cookieProvider: CookieProvider,
    private val musicServiceConnection: MusicServiceConnection,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val loggedUser: LoggedUserRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileScreenState())

     init {
        viewModelScope.launch {

            // TODO this if statement is workaround because sometimes
            // login works incorrect and username remains null
            if (loggedUser.name.value == null) {
                state = state.copy(
                    isLoading = false,
                    isError = true
                )
            } else {
                getUserInfoUseCase
                    .invoke(loggedUser.name.value!!)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let {
                                    state = state.copy(
                                        isLoading = false,
                                        isError = false,
                                        username = it.username,
                                        imageUrl = it.imageUrl
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                state = state.copy(isLoading = true)
                            }
                            is Resource.Error -> {
                                state = state.copy(
                                    isLoading = false,
                                    isError = true
                                )
                            }
                        }
                    }
            }
        }
    }


    fun onLogout() {
        loggedUser.logOut()
        musicServiceConnection.sendCommand(MediaConstants.STOP_PLAYER, null)
    }
}

data class ProfileScreenState(
    val isLoading: Boolean = true,

    val isError: Boolean = false,

    val username: String = "",
    val imageUrl: String = "",

)