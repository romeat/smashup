package com.romeat.smashup.domain

import com.romeat.smashup.data.dto.OwnProfile
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class GetUserInfoUseCaseImpl  @Inject constructor(
    private val remoteData: SmashupRemoteData
) : GetUserInfoUseCase {
    override suspend fun invoke(username: String): Flow<Resource<OwnProfile>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getUserInfo(username)
                if (response.isSuccessful) {
                    response.body()!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}