package com.romeat.smashup.domain.user.profile

import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class ChangeUserNameUseCase @Inject constructor(
    private val remoteData: SmashupRemoteData
) {
    suspend operator fun invoke(password: String, newUsername: String): Flow<Resource<Unit>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.changeUserName(password, newUsername)
                if (!response.isSuccessful) {
                    throw HttpException(response)
                }
            }
        )
}