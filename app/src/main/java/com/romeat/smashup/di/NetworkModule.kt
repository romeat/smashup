package com.romeat.smashup.di

import com.romeat.smashup.BuildConfig
import com.romeat.smashup.network.*
import com.romeat.smashup.network.util.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideAuthNetwork() : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .client(
            OkHttpClient
                .Builder()
                .callTimeout(12, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
        )
        .build()


    @Provides
    @Singleton
    @MainRetrofitClient
    fun provideMainNetwork(interceptor: RequestInterceptor) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .client(
            OkHttpClient
                .Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
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