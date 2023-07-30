package com.romeat.smashup.domain.user.settings

import com.romeat.smashup.data.dto.SmashupSettings
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SmashupApiException
import com.romeat.smashup.util.getResourceWithExceptionLogging
import com.romeat.smashup.util.toApiWrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    val remoteData: SmashupRemoteData
) {
    suspend operator fun invoke(): Flow<Resource<SmashupSettings>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getUserSettings()
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