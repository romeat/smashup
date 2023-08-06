package com.romeat.smashup.network

import com.google.gson.annotations.SerializedName
import com.romeat.smashup.data.dto.LoginRequest
import com.romeat.smashup.data.dto.LoginResponse
import com.romeat.smashup.network.util.ApiWrap
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    /* Login */
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ApiWrap<LoginResponse>>


    /* Register */
    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterBody
    ): Response<ApiWrap<Unit>>

    @POST("register/confirm")
    suspend fun registerConfirm(
        @Query("id") id: String
    ): Response<ApiWrap<LoginResponse>>


    /* Recover password */
    @POST("user/recover_password")
    suspend fun recoverPassword(
        @Body body: RecoverPasswordBody
    ): Response<ApiWrap<Unit>>

    @POST("user/recover_password/confirm")
    suspend fun recoverPasswordConfirm(
        @Body body: RecoverPasswordConfirmBody
    ): Response<ApiWrap<Unit>>

}

data class RegisterBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)


data class RecoverPasswordBody(
    @SerializedName("username")
    val usernameOrEmail: String
)

data class RecoverPasswordConfirmBody(
    @SerializedName("id")
    val id: String,
    @SerializedName("newPassword")
    val newPassword: String
)
