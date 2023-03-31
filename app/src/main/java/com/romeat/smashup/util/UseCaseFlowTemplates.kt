package com.romeat.smashup.util

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

suspend fun <T> getResourceWithExceptionLogging(
    action: suspend () -> T, // "suspending lambda"
    dispatcher: CoroutineDispatcher
): Flow<Resource<T>> {
    return flow<Resource<T>> {
        try {
            emit(Resource.Loading())
            val result = action()
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            Log.e(Constants.LOG_TAG, e.message())
            Log.e(Constants.LOG_TAG, e.stackTraceToString())
            emit(Resource.Error(e, "Http exception"))
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.message ?: "No message")
            Log.e(Constants.LOG_TAG, e.stackTraceToString())
            emit(Resource.Error(e, "Unknown exception"))
        }
    }.flowOn(dispatcher)
}
