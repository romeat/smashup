package com.romeat.smashup.domain.auth

import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class ConfirmPasswordRecovery @Inject constructor(
    private val remoteData: SmashupAuthData
) {
    suspend operator fun invoke(token: String, newPassword: String): Flow<Resource<Boolean>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.recoverPasswordConfirm(token, newPassword)
                if (response.isSuccessful) {
                    true
                } else {
                    throw HttpException(response)
                }
            }
        )
}