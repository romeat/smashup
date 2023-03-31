package com.romeat.smashup.domain

import com.romeat.smashup.network.SmashupAuthData
import retrofit2.Response
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val remoteData: SmashupAuthData
) : LoginUseCase {
    override suspend fun invoke(username: String, password: String): Response<String> = remoteData.login(username, password)
}