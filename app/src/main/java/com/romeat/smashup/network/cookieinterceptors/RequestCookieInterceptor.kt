package com.romeat.smashup.network.cookieinterceptors

import com.romeat.smashup.data.CookieProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RequestCookieInterceptor @Inject constructor(
    private val cookieProvider: CookieProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val cookies = cookieProvider.getCookiesSet()
        for (cookie in cookies) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }
}