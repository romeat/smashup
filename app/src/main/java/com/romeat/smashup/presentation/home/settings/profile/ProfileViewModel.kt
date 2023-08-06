package com.romeat.smashup.presentation.home.settings.profile

import androidx.lifecycle.ViewModel
import com.romeat.smashup.data.LoggedUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loggedUser: LoggedUserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        val user = loggedUser.userInfoFlow.value
        user?.let { presentUser ->
            _state.update {
                it.copy(
                    nickname = presentUser.username,
                    email = "блять, емаил неоткуда взять",
                    passwordDots = "***********"
                )
            }
        }
    }
}

data class ProfileState(
    val nickname: String = "",
    val email: String = "",
    val passwordDots: String = "",
    val imageUrl: String = ""
)