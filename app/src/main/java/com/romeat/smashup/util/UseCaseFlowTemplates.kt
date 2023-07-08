package com.romeat.smashup.util

import android.util.Log
import com.google.gson.Gson
import com.romeat.smashup.network.util.ApiWrap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T> getResourceWithExceptionLogging(
    action: suspend () -> T, // "suspending lambda"
    dispatcher: CoroutineDispatcher
): Flow<Resource<T>> {
    return flow<Resource<T>> {
        try {
            emit(Resource.Loading())
            val result = action()
            emit(Resource.Success(result))
        } catch (e: SmashupApiException) {
            Log.e(Constants.LOG_TAG, e.stackTraceToString())
            emit(Resource.Error(e, e.code, e.data.message, e.data))
        } catch (e: HttpException) {
            Log.e(Constants.LOG_TAG, e.message())
            Log.e(Constants.LOG_TAG, e.stackTraceToString())
            emit(Resource.Error(e, e.code(),"Http exception: ${e.message}",))
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message ?: "Unknown exception")
            Log.e(Constants.LOG_TAG, e.stackTraceToString())
            emit(Resource.Error(e, 999, null))
        }
    }.flowOn(dispatcher)
}

fun Response<*>.toApiWrap(): ApiWrap<*> {
    return Gson().fromJson(this.errorBody()!!.charStream(), ApiWrap::class.java)
}

class SmashupApiException(
    val data: ApiWrap<*>,
    val code: Int,
) : Exception(data.message)
