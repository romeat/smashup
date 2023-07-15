package com.romeat.smashup.domain.author

import com.romeat.smashup.data.dto.UserProfile
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetAuthorUseCase {
    suspend operator fun invoke(name: String): Flow<Resource<UserProfile>>
}