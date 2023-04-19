package com.romeat.smashup.presentation.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.SettingsProvider
import com.romeat.smashup.domain.GetUserInfoUseCase
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.MediaConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val loggedUser: LoggedUserRepository,
    private val settingsProvider: SettingsProvider
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loggedUser.fullInfo.collect{ profile ->
                profile?.let {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            username = profile.username,
                            imageUrl = profile.imageUrl
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            settingsProvider.bitrate.collect { option ->
                _state.update {
                    it.copy(selectedBitrate = option)
                }
            }
        }
        viewModelScope.launch {
            settingsProvider.explicitAllowed.collect { value ->
                _state.update {
                    it.copy(explicitAllowed = value)
                }
            }
        }
    }

    fun onBitrateOptionSelect(newOption: BitrateOption) {
        viewModelScope.launch {
            settingsProvider.updateBitrate(newOption)
        }
    }

    fun onLanguageOptionSelect() {

    }

    fun onExplicitToggle() {
        viewModelScope.launch {
            settingsProvider.updateExplicit(!state.value.explicitAllowed)
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

    val selectedBitrate: BitrateOption = BitrateOption.OrigQuality,
    val bitrateOptions: List<BitrateOption> = listOf(
        BitrateOption.OrigQuality,
        BitrateOption.KB160,
        BitrateOption.KB128,
        BitrateOption.KB64
    ),

    val explicitAllowed: Boolean = true
)