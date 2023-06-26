package com.romeat.smashup.domain

import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.util.Resource
import retrofit2.HttpException
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val remoteData: SmashupAuthData
) : LoginUseCase {
    override suspend fun invoke(username: String, password: String): Resource<LoginResponse> {
        val response = remoteData.login(username, password)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!.response)
        } else {
            Resource.Error(
                exception = HttpException(response),
                message = response.raw().message,
                code = response.code()
            )
        }
    }
}

