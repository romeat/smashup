package com.romeat.smashup.domain.auth

import android.util.Log
import com.google.gson.Gson
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.network.util.ApiWrap
import com.romeat.smashup.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val remoteData: SmashupAuthData
) {
    suspend operator fun invoke(username: String, email: String, password: String): Flow<Resource<Boolean>>
//    {
//        val response = remoteData.register(username, email, password)
//        return if (response.isSuccessful) {
//            Resource.Success(true)
//        } else {
//            val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ApiWrap::class.java)
//            Resource.Error(
//                exception = HttpException(response),
//                message = errorResponse?.message ?: "unknown error",
//                code = response.code()
//            )
//        }
//    }
    =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.register(username, email, password)
                if (response.isSuccessful) {
                    true
                } else {
                    throw SmashupApiException(
                        response.toApiWrap(),
                        response.code()
                    )
                    //val resp2 = Gson().fromJson(response.errorBody()!!.charStream(), ApiWrap::class.java)
                    //Log.e(Constants.LOG_TAG, resp2.message ?: "fuck")
                    //throw HttpException(response)
                }
            }
        )
}