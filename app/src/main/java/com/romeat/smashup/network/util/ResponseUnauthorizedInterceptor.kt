package com.romeat.smashup.network.util

import com.romeat.smashup.data.LoggedUserRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ResponseUnauthorizedInterceptor @Inject constructor(
    private val userRepository: LoggedUserRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            userRepository.logout()
        }
        return response
    }
}