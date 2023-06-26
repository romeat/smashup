package com.romeat.smashup.network

import com.romeat.smashup.data.LoggedUserRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    private val userRepository: LoggedUserRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        userRepository.userInfoFlow.value?.token?.let {
            builder.addHeader("Bearer", it)
        }
        return chain.proceed(builder.build())
    }
}