package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetMashupsWithSourceUseCase {
    suspend operator fun invoke(sourceId: Int): Flow<Resource<List<Mashup>>>
}