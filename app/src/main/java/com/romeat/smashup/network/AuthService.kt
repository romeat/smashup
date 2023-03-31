package com.romeat.smashup.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("username") username: String,
                      @Field("password") password: String,
                      @Field("submit") submit: String
    ): Response<String>
}