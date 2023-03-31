package com.romeat.smashup.network

import javax.inject.Inject

class SmashupAuthData @Inject constructor(
    private val authService : AuthService
) {
    suspend fun login(login: String, password: String) =
        authService.login(login, password, "submit")
}