package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class GetMashupsWithSourceUseCaseImpl @Inject constructor(
    private val remoteData: SmashupRemoteData
) : GetMashupsWithSourceUseCase {
    override suspend fun invoke(sourceId: Int): Flow<Resource<List<Mashup>>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getMashupsWithSource(sourceId)
                if (response.isSuccessful) {
                    response.body()!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}