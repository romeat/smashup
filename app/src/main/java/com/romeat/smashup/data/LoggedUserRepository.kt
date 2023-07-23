package com.romeat.smashup.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.musicservice.MusicServiceConnection
import com.romeat.smashup.util.MediaConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedUserRepository @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val musicServiceConnection: MusicServiceConnection,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smashup_user")

    private val USER_KEY = stringPreferencesKey("user_data")

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val USER_PREFS_FILE = "UserPrefsFile"
    private val USER_PREFS_NAME = "UserName"

    val userInfoFlow: StateFlow<LoginResponse?> = appContext.dataStore.data
        .map { pref ->
            Gson().fromJson(pref[USER_KEY], LoginResponse::class.java)
        }.stateIn(scope, SharingStarted.Eagerly, null)

    suspend fun updateUserStat(user: LoginResponse) {
        appContext.dataStore.edit { pref ->
            pref[USER_KEY] = Gson().toJson(user)
        }
        scope.launch {
            delay(1000)
            // todo send fcm token update request

        }
    }

    fun logout() {
        scope.launch {
            appContext.dataStore.edit { pref ->
                pref.remove(USER_KEY)
            }
        }
        scope.launch(Dispatchers.Main) {
            musicServiceConnection.sendCommand(MediaConstants.STOP_PLAYER, null)
        }
    }
}


