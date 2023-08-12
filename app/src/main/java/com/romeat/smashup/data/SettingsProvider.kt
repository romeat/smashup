package com.romeat.smashup.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.romeat.smashup.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SettingsProvider @Inject constructor(
    @ApplicationContext val context: Context,
    val loggedUserRepository: LoggedUserRepository
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smashup_settings")

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val bitrateSuffix = "_bitrate_setting"
    private val defaultBitrate = BitrateOption.KB160

    // TODO create BiMap for this data
    private val bitrateToDatastoreStringMap = hashMapOf(
        BitrateOption.KB64 to "KB64",
        BitrateOption.KB96 to "KB96",
        BitrateOption.KB128 to "KB128",
        BitrateOption.KB160 to "KB160",
        BitrateOption.KB320 to "KB320",
        //BitrateOption.OrigQuality to "OrigQuality",
    )
    private val datastoreStringToBitrateMap = hashMapOf(
        "KB64" to BitrateOption.KB64,
        "KB96" to BitrateOption.KB96,
        "KB128" to BitrateOption.KB128,
        "KB160" to BitrateOption.KB160,
        "KB320" to BitrateOption.KB320,
        //"OrigQuality" to BitrateOption.OrigQuality,
    )

    private val explicitSuffix = "_explicit_content_setting"
    private val defaultExplicitAllowed = true

    val bitrate: StateFlow<BitrateOption> =
        context.dataStore.data
            .combine(loggedUserRepository.userInfoFlow) { datastoreFlow, userInfo ->
                Pair(datastoreFlow, userInfo)
            }.map { pair ->
                if (pair.second == null)
                    defaultBitrate
                else {
                    val BITRATE =
                        stringPreferencesKey(pair.second!!.username + bitrateSuffix)
                    datastoreStringToBitrateMap[pair.first[BITRATE]] ?: defaultBitrate
                }
            }.stateIn(scope, SharingStarted.Eagerly, defaultBitrate)

    val explicitAllowed: StateFlow<Boolean> =
        context.dataStore.data
            .combine(loggedUserRepository.userInfoFlow) { datastoreFlow, userInfo ->
                Pair(datastoreFlow, userInfo)
            }.map { pair ->
                if (pair.second == null)
                    defaultExplicitAllowed
                else {
                    val EXPLICIT =
                        booleanPreferencesKey(pair.second!!.username + explicitSuffix)
                    pair.first[EXPLICIT] ?: defaultExplicitAllowed
                }
            }.stateIn(scope, SharingStarted.Eagerly, defaultExplicitAllowed)

    // warning - locale is not tied to any user
    private val _locale = MutableStateFlow(getCurrentLocale())
    val locale: StateFlow<LanguageOption> = _locale.asStateFlow()

    private fun getCurrentLocale(): LanguageOption {
        val locale = AppCompatDelegate.getApplicationLocales()[0]

        // when locale is not set by user explicitly (== null), app uses default system locale
        val language = locale?.language ?: context.resources.configuration.locales[0].language

        return when (language) {
            LanguageOption.RUS.languageTag -> LanguageOption.RUS
            else -> LanguageOption.ENG
        }
    }

    suspend fun updateBitrate(newBitrate: BitrateOption) {
        context.dataStore.edit { settings ->
            val BITRATE = stringPreferencesKey(loggedUserRepository.userInfoFlow.value!!.username + bitrateSuffix)
            settings[BITRATE] = bitrateToDatastoreStringMap[newBitrate]!!
        }
    }

    suspend fun updateExplicit(allowExplicit: Boolean) {
        context.dataStore.edit { settings ->
            val EXPLICIT = booleanPreferencesKey(loggedUserRepository.userInfoFlow.value!!.username + explicitSuffix)
            settings[EXPLICIT] = allowExplicit
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateLanguage(newLanguageOption: LanguageOption) {
        _locale.value = newLanguageOption

        val locale = LocaleListCompat.forLanguageTags(newLanguageOption.languageTag)
        GlobalScope.launch(Dispatchers.Main) {
            AppCompatDelegate.setApplicationLocales(locale)
        }
    }
}

interface SettingItemOption {
    val displayResId: Int
}

sealed class BitrateOption(
    override val displayResId: Int,
    val suffix: String,
) : SettingItemOption {
    object KB64 : BitrateOption(R.string.bitrate64, "64000")
    object KB96 : BitrateOption(R.string.bitrate96, "96000")
    object KB128 : BitrateOption(R.string.bitrate128, "128000")
    object KB160 : BitrateOption(R.string.bitrate160, "160000")
    object KB320 : BitrateOption(R.string.bitrate320, "320000")
    //object OrigQuality : BitrateOption(R.string.bitrate_orig, ".mp3")
}

sealed class LanguageOption(
    override val displayResId: Int,
    val languageTag: String
) : SettingItemOption {
    object RUS : LanguageOption(R.string.lang_RU, "ru")
    object ENG : LanguageOption(R.string.lang_EN, "en")
}