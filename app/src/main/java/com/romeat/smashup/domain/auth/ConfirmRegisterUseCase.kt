package com.romeat.smashup.domain.auth

import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class ConfirmRegisterUseCase @Inject constructor(
    private val remoteData: SmashupAuthData
) {
    suspend operator fun invoke(token: String): Flow<Resource<LoginResponse>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.registerConfirm(token)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}