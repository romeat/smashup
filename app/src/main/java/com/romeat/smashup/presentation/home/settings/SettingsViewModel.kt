package com.romeat.smashup.presentation.home.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romeat.smashup.R
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LanguageOption
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.SettingsProvider
import com.romeat.smashup.domain.user.settings.GetSettingsUseCase
import com.romeat.smashup.domain.user.settings.UpdateMultisessionSettingUseCase
import com.romeat.smashup.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val loggedUser: LoggedUserRepository,
    private val settingsProvider: SettingsProvider,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateMultisessionSettingUseCase: UpdateMultisessionSettingUseCase,
) : ViewModel() {

    private val BIT_EXPLICIT_ALLOWED: Int = 0b00000001 // not used
    private val BIT_MULTISESSIONS: Int = 0b00000010

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<SettingsEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getSettingsUseCase
                .invoke()
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            eventChannel.send(SettingsEvent.ShowToast(R.string.toast_failed_to_load_settings))
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    allowMultisessions = (result.data!!.bits and BIT_MULTISESSIONS > 0)
                                )
                            }
                        }
                    }

                }
        }

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

    fun updateMultisession(allowed: Boolean) {
        viewModelScope.launch {
            updateMultisessionSettingUseCase
                .invoke(allowed)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            eventChannel.send(SettingsEvent.ShowToast(R.string.toast_failed_to_update_setting))
                        }
                        is Resource.Loading -> { }
                        is Resource.Success -> {
                            _state.update {
                                it.copy(allowMultisessions = (result.data!!.bits and BIT_MULTISESSIONS > 0))
                            }
                            eventChannel.send(SettingsEvent.ShowToast(R.string.toast_setting_updated))
                        }
                    }
                }
        }
    }

    fun onLogout() {
        loggedUser.logout()
    }
}

data class SettingsState(

    val isLoading: Boolean = true,

    val username: String = "",
    val imageUrl: String = "",

    val newsNotificationsAllowed: Boolean = false,
    val systemNotificationsAllowed: Boolean = false,

    val allowMultisessions: Boolean = false,
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

sealed class SettingsEvent {
    data class ShowToast(val toastRes: Int) : SettingsEvent()
}