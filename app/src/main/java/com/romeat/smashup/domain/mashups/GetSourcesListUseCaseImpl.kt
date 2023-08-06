package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetSourcesListUseCaseImpl @Inject constructor(
    private val remoteData: SmashupRemoteData
) : GetSourcesListUseCase {
    override suspend fun invoke(sourceIds: List<Int>): Flow<Resource<List<Source>>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getSourcesList(sourceIds)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}