package com.romeat.smashup.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.romeat.smashup.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsProvider @Inject constructor(
    @ApplicationContext val context: Context,
    val loggedUserRepository: LoggedUserRepository
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smashup_settings")

    private val bitrateSuffix = "_bitrate_setting"
    private val defaultBitrate = BitrateOption.OrigQuality

    // TODO create BiMap for this data
    private val bitrateToDatastoreStringMap = hashMapOf(
        BitrateOption.KB64 to "KB64",
        BitrateOption.KB128 to "KB128",
        BitrateOption.KB160 to "KB160",
        BitrateOption.OrigQuality to "OrigQuality",
    )
    private val datastoreStringToBitrateMap = hashMapOf(
        "KB64" to BitrateOption.KB64,
        "KB128" to BitrateOption.KB128,
        "KB160" to BitrateOption.KB160,
        "OrigQuality" to BitrateOption.OrigQuality,
    )

    private val explicitSuffix = "_explicit_content_setting"
    private val defaultExplicitAllowed = true

    @OptIn(DelicateCoroutinesApi::class)
    val bitrate: StateFlow<BitrateOption> =
        context.dataStore.data
            .map { preferences ->
                if (loggedUserRepository.name.value.isNullOrEmpty())
                    defaultBitrate
                else {
                    val BITRATE =
                        stringPreferencesKey(loggedUserRepository.name.value + bitrateSuffix)
                    datastoreStringToBitrateMap[preferences[BITRATE]] ?: defaultBitrate
                }
            }.stateIn(GlobalScope, SharingStarted.Eagerly, defaultBitrate)

    @OptIn(DelicateCoroutinesApi::class)
    val explicitAllowed: StateFlow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                if (loggedUserRepository.name.value.isNullOrEmpty())
                    defaultExplicitAllowed
                else {
                    val EXPLICIT =
                        booleanPreferencesKey(loggedUserRepository.name.value + explicitSuffix)
                    preferences[EXPLICIT] ?: defaultExplicitAllowed
                }
            }.stateIn(GlobalScope, SharingStarted.Eagerly, defaultExplicitAllowed)


    suspend fun updateBitrate(newBitrate: BitrateOption) {
        context.dataStore.edit { settings ->
            val BITRATE = stringPreferencesKey(loggedUserRepository.name.value + bitrateSuffix)
            settings[BITRATE] = bitrateToDatastoreStringMap[newBitrate]!!
        }
    }

    suspend fun updateExplicit(allowExplicit: Boolean) {
        context.dataStore.edit { settings ->
            val EXPLICIT = booleanPreferencesKey(loggedUserRepository.name.value + explicitSuffix)
            settings[EXPLICIT] = allowExplicit
        }
    }

    suspend fun updateLanguage() {

    }
}

sealed class BitrateOption(
    val displayStringRes: Int,
    val suffix: String,
) {
    object KB64 : BitrateOption(R.string.bitrate64, "_64000.mp3")
    object KB128 : BitrateOption(R.string.bitrate128, "_128000.mp3")
    object KB160 : BitrateOption(R.string.bitrate160, "_160000.mp3")
    object OrigQuality : BitrateOption(R.string.bitrate_orig, ".mp3")
}

sealed class LanguageOption(val stringRes: Int) {
    object RUS : LanguageOption(R.string.lang_RU)
    object ENG : LanguageOption(R.string.lang_EN)
}