package com.romeat.smashup.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.data.likes.UserLikesHolder
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.network.SmashupRemoteData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedUserRepository @Inject constructor(
    @ApplicationContext val appContext: Context,
    //private val remoteData: SmashupRemoteData,
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

    fun logout() = scope.launch {
        // todo send fcm token remove request

        appContext.dataStore.edit { pref ->
            pref.remove(USER_KEY)
        }
    }

    /*
    fun updateUserStat(user: LoginResponse) {
        _name.value = appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .getString(USER_PREFS_NAME, null)
        //getUserInfo()
    }
     */



    /*
    fun setName(user: String) {
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(
                USER_PREFS_NAME,
                user
            )
            .apply()
    }

     */



    /*
    @OptIn(DelicateCoroutinesApi::class)
    private fun getUserInfo() {
        GlobalScope.launch {
            getUserInfoUseCase
                .invoke(_name.value!!)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _fullInfo.value = it
                                userLikes.updateLoggedUserLikes(it.mashupsLikes)
                            }
                        }
                        is Resource.Loading -> { }
                        is Resource.Error -> {
                            _fullInfo.value = null
                            userLikes.updateLoggedUserLikes(emptyList())
                        }
                    }
                }
        }
    }
     */

    /*
    fun logOut() {
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .remove(USER_PREFS_NAME)
            .apply()
        _name.value = null
        _fullInfo.value = null
    }

     */
}


