package com.romeat.smashup.data

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.romeat.smashup.domain.user.RemoveFirebaseTokenUseCase
import com.romeat.smashup.domain.user.UpdateFirebaseTokenUseCase
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmashupFirebase @Inject constructor(
    private val appContext: Context,
    val loggedUserRepository: LoggedUserRepository,
    private val updateFirebaseTokenUseCase: UpdateFirebaseTokenUseCase,
    private val removeFirebaseTokenUseCase: RemoveFirebaseTokenUseCase,
) : FirebaseMessagingService() {

    private val VERSION_PREFS_FILE = "TokenInfoPrefsFile"
    private val TOKEN_LAST_UPDATED_TIME = "LastUpdatedTime"
    private val TOKEN_LAST_KNOWN_USER_ID = "LastKnownUserId"

    private val NO_USER = -1

    private val threshold = TimeUnit.DAYS.toMillis(4) // min period between token updates - 4 days

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SMASHUP", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            scope.launch {
                loggedUserRepository.userInfoFlow.collect { user ->
                    val timeNow = System.currentTimeMillis()
                    if (user == null) {
                        if (getLastKnownUserId() != NO_USER) { // user logged out
                            deleteToken(token)
                        }
                    } else { // new user logged in or launched app
                        if (getLastKnownUserId() == user.id) { // user already known (ordinary launch)
                            if (timeNow - getLastCheckedTime() > threshold) {
                                // update only if threshold exceeded
                                updateToken(
                                    token = token,
                                    onSuccess = {
                                        updateLastCheckedTime(timeNow)
                                    }
                                )
                            }
                        } else { // fresh user logged in
                            updateToken(
                                token = token,
                                onSuccess = {
                                    updateLastCheckedTime(timeNow)
                                    updateLastKnownUserId(user.id)
                                }
                            )
                        }
                    }
                }
            }
        })
    }

    private fun deleteToken(token: String) {
        scope.launch {
            removeFirebaseTokenUseCase
                .invoke(token)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            updateLastKnownUserId(NO_USER)
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun updateToken(token: String, onSuccess: () -> Unit) {
        scope.launch {
            updateFirebaseTokenUseCase
                .invoke(token)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            onSuccess()
                        }
                        else -> {}
                    }
                }
        }
    }


    private fun getLastCheckedTime(): Long {
        return appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .getLong(TOKEN_LAST_UPDATED_TIME, 0)
    }

    private fun updateLastCheckedTime(timestamp: Long) {
        appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putLong(TOKEN_LAST_UPDATED_TIME, timestamp)
            .apply()
    }

    private fun getLastKnownUserId(): Int {
        return appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .getInt(TOKEN_LAST_KNOWN_USER_ID, NO_USER)
    }

    private fun updateLastKnownUserId(id: Int) {
        appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putInt(TOKEN_LAST_KNOWN_USER_ID, id)
            .apply()
    }
}