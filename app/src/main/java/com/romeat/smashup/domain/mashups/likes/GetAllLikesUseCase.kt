package com.romeat.smashup.domain.mashups.likes

import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SmashupApiException
import com.romeat.smashup.util.getResourceWithExceptionLogging
import com.romeat.smashup.util.toApiWrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetAllLikesUseCase @Inject constructor(
    private val remoteData: SmashupRemoteData
) {
    suspend fun invoke(): Flow<Resource<List<Int>>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getMyLikes()
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