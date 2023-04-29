package com.romeat.smashup.data

import android.content.Context
import com.romeat.smashup.data.dto.OwnProfile
import com.romeat.smashup.data.likes.UserLikesHolder
import com.romeat.smashup.domain.GetUserInfoUseCase
import com.romeat.smashup.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedUserRepository @Inject constructor(
    @ApplicationContext val appContext: Context,
    val cookieProvider: CookieProvider,
    val getUserInfoUseCase: GetUserInfoUseCase,
    val userLikes: UserLikesHolder
) {
    private val USER_PREFS_FILE = "UserPrefsFile"
    private val USER_PREFS_NAME = "UserName"

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _fullInfo = MutableStateFlow<OwnProfile?>(null)
    val fullInfo = _fullInfo.asStateFlow()

    fun isCookieReceived(): Boolean {
        return cookieProvider.getCookiesSet().isNotEmpty()
    }

    fun invalidateCookie() {
        cookieProvider.clearAuthCookies()
    }

    fun setName(user: String) {
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(
                USER_PREFS_NAME,
                user
            )
            .apply()
        updateUserStat()
    }

    fun updateUserStat() {
        _name.value = appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .getString(USER_PREFS_NAME, null)
        getUserInfo()
    }

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

    fun logOut() {
        invalidateCookie()
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .remove(USER_PREFS_NAME)
            .apply()
        _name.value = null
        _fullInfo.value = null
    }
}