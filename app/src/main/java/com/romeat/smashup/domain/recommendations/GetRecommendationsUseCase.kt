package com.romeat.smashup.domain.recommendations

import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val remoteData: SmashupRemoteData
) {
    suspend operator fun invoke(): Flow<Resource<List<Int>>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getRecommendationsMashupIds()
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}