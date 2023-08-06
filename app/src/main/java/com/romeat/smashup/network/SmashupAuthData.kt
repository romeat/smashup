package com.romeat.smashup.network

import com.romeat.smashup.data.dto.LoginRequest
import javax.inject.Inject

class SmashupAuthData @Inject constructor(
    private val authService : AuthService
) {
    suspend fun login(login: String, password: String) =
        authService.login(LoginRequest(login, password))

    suspend fun register(username: String, email: String, password: String) =
        authService.register(RegisterBody(username, email, password))

    suspend fun registerConfirm(token: String) =
        authService.registerConfirm(id = token)

    suspend fun recoverPassword(usernameOrEmail: String) =
        authService.recoverPassword(RecoverPasswordBody(usernameOrEmail))

    suspend fun recoverPasswordConfirm(token: String, newPassword: String) =
        authService.recoverPasswordConfirm(RecoverPasswordConfirmBody(token, newPassword))
}