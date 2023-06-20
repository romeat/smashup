package com.romeat.smashup.presentation.home.settings

import androidx.lifecycle.ViewModel
import com.romeat.smashup.data.BitrateOption
import com.romeat.smashup.data.LanguageOption
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.data.SettingsProvider
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.presentation.home.profile.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val loggedUser: LoggedUserRepository,
    private val settingsProvider: SettingsProvider
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

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
        BitrateOption.KB320,
        BitrateOption.KB160,
        BitrateOption.KB128,
        BitrateOption.KB96,
        BitrateOption.KB64
    ),

    val selectedLanguage: LanguageOption = LanguageOption.ENG,
    val languageOptions: List<LanguageOption> = listOf(
        LanguageOption.ENG,
        LanguageOption.RUS
    )
)