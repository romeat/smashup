package com.romeat.smashup.domain.mashups.likes

import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface AddLikeUseCase {
    suspend operator fun invoke(mashupId: Int): Flow<Resource<Boolean>>
}