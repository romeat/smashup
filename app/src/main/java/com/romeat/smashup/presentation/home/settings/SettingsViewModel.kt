package com.romeat.smashup.presentation.home.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LanguageOption
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.SettingsProvider
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.presentation.home.profile.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val loggedUser: LoggedUserRepository,
    private val settingsProvider: SettingsProvider
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loggedUser.userInfoFlow.collect { profile ->
                profile?.let {
                    _state.update {
                        it.copy(
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
        viewModelScope.launch {
            settingsProvider.locale.collect { value ->
                _state.update {
                    it.copy(selectedLanguage = value)
                }
            }
        }
    }

    fun onBitrateOptionSelect(newOption: BitrateOption) {
        viewModelScope.launch {
            settingsProvider.updateBitrate(newOption)
        }
    }

    fun onLanguageOptionSelect(newLanguageOption: LanguageOption) {
        settingsProvider.updateLanguage(newLanguageOption)
    }

    fun onExplicitToggle() {
        viewModelScope.launch {
            settingsProvider.updateExplicit(!state.value.explicitAllowed)
        }
    }
}

data class SettingsState(

    val username: String = "",
    val imageUrl: String = "",

    val newsNotificationsAllowed: Boolean = false,
    val systemNotificationsAllowed: Boolean = false,

    val allowMultisessions: Boolean = true,
    val explicitAllowed: Boolean = true,

    val selectedBitrate: BitrateOption = BitrateOption.KB320,
    val bitrateOptions: List<BitrateOption> = listOf(
        BitrateOption.KB64,
        BitrateOption.KB96,
        BitrateOption.KB128,
        BitrateOption.KB160,
        BitrateOption.KB320,
    ),

    val selectedLanguage: LanguageOption = LanguageOption.ENG,
    val languageOptions: List<LanguageOption> = listOf(
        LanguageOption.ENG,
        LanguageOption.RUS
    )
)