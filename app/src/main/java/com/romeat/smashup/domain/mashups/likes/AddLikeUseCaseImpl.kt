package com.romeat.smashup.domain.mashups.likes

import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class AddLikeUseCaseImpl @Inject constructor(
    private val remoteData: SmashupRemoteData
) : AddLikeUseCase {
    override suspend fun invoke(mashupId: Int): Flow<Resource<Boolean>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.addLikeToMashup(mashupId)
                if (response.isSuccessful) {
                    true
                } else {
                    throw HttpException(response)
                }
            }
        )
}