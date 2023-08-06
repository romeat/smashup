package com.romeat.smashup.domain.user

import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SmashupApiException
import com.romeat.smashup.util.getResourceWithExceptionLogging
import com.romeat.smashup.util.toApiWrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    val remoteData: SmashupRemoteData
) : GetUserUseCase {
    override suspend fun invoke(id: Int): Flow<Resource<UserProfile>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getAuthorInfo(id)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw SmashupApiException(
                        response.toApiWrap(),
                        response.code()
                    )
                }
            }
        )
}