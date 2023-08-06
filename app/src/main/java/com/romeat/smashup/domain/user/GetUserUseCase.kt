package com.romeat.smashup.domain.user

import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetUserUseCase {
    suspend operator fun invoke(id: Int): Flow<Resource<UserProfile>>
}