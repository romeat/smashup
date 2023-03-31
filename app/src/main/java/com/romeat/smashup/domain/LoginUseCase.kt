package com.romeat.smashup.domain

import com.romeat.smashup.data.dto.LoginResponse
import retrofit2.Response

interface LoginUseCase {
    suspend operator fun invoke(username: String, password: String): Response<String>
}