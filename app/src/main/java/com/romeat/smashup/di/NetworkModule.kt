package com.romeat.smashup.di

import com.romeat.smashup.network.AuthService
import com.romeat.smashup.network.MainService
import com.romeat.smashup.network.SmashupAuthData
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.network.cookieinterceptors.RequestCookieInterceptor
import com.romeat.smashup.network.cookieinterceptors.ResponseCookieInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorRetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRetrofitClient

    @Provides
    @Singleton
    @AuthInterceptorRetrofitClient
    fun provideAuthNetwork(interceptor: ResponseCookieInterceptor) : Retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl("https://smashup.ru/")
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build()
        )
        .build()


    @Provides
    @Singleton
    @MainRetrofitClient
    fun provideMainNetwork(interceptor: RequestCookieInterceptor) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://smashup.ru/")
        .client(
            OkHttpClient
                .Builder()
                .addNetworkInterceptor(interceptor)
                .build()
        )
        .build()


    @Provides
    @Singleton
    fun provideMainService(
        @MainRetrofitClient retrofit : Retrofit
    ) : MainService = retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(
        @AuthInterceptorRetrofitClient retrofit : Retrofit
    ) : AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideMainRemoteData(mainService : MainService) : SmashupRemoteData = SmashupRemoteData(mainService)

    @Provides
    @Singleton
    fun provideAuthRemoteData(authService : AuthService) : SmashupAuthData = SmashupAuthData(authService)

}