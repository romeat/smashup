package com.romeat.smashup.network.cookieinterceptors

import android.content.Context
import com.romeat.smashup.di.NetworkModule
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/*
class ResponseCookieInterceptor @Inject constructor(
    private val cookieProvider: CookieProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse: Response = chain.proceed(chain.request())
            if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
                val cookies: HashSet<String> = HashSet()
                for (header in originalResponse.headers("Set-Cookie")) {
                    cookies.add(header)
                }

                cookieProvider.setCookies(cookies)
            }
            return originalResponse
    }
}

 */