package com.romeat.smashup.domain.auth

import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val remoteData: SmashupAuthData
) {
    suspend operator fun invoke(username: String, email: String, password: String): Flow<Resource<Boolean>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.register(username, email, password)
                if (response.isSuccessful) {
                    true
                } else {
                    throw HttpException(response)
                }
            }
        )
}