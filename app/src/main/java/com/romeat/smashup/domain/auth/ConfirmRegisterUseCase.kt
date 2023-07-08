package com.romeat.smashup.domain.auth

import com.google.gson.Gson
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.network.util.ApiWrap
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.SmashupApiException
import com.romeat.smashup.util.getResourceWithExceptionLogging
import com.romeat.smashup.util.toApiWrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class ConfirmRegisterUseCase @Inject constructor(
    private val remoteData: SmashupAuthData
) {
    suspend operator fun invoke(token: String): Flow<Resource<LoginResponse>>
    /*{
        val response = remoteData.registerConfirm(token)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!.response)
        } else {
            val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ApiWrap::class.java)
            Resource.Error(
                exception = HttpException(response),
                message = errorResponse?.message ?: "unknown error",
                code = response.code()
            )
        }
    }

     */
    =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.registerConfirm(token)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    //val resp2 = Gson().fromJson(response.errorBody()!!.charStream(), ApiWrap::class.java)
                    throw SmashupApiException(response.toApiWrap(), response.code())
                    //throw HttpException(response)
                }
            }
        )
}