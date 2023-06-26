package com.romeat.smashup.network

import com.romeat.smashup.data.dto.LoginRequest
import javax.inject.Inject

class SmashupAuthData @Inject constructor(
    private val authService : AuthService
) {
    suspend fun login(login: String, password: String) =
        authService.login(LoginRequest(login, password))
}