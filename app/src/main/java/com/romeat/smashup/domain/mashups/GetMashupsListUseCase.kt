package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetMashupsListUseCase {
    suspend operator fun invoke(mashupIds: List<Int>): Flow<Resource<List<Mashup>>>
}