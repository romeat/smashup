package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetSourceUseCase {
    suspend operator fun invoke(sourceId: Int): Flow<Resource<List<Source>>>
}