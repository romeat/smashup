package com.romeat.smashup.domain.author

import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetAuthorUseCaseImpl @Inject constructor(
    val remoteData: SmashupRemoteData
) : GetAuthorUseCase {
    override suspend fun invoke(name: String): Flow<Resource<UserProfile>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getAuthorInfo(name)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}