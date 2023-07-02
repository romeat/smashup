package com.romeat.smashup.network

import com.romeat.smashup.data.dto.LoginRequest
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.network.util.ApiWrap
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ApiWrap<LoginResponse>>
}