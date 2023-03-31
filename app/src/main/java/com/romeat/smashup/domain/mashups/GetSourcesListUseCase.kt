package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetSourcesListUseCase {
    suspend operator fun invoke(sourceIds: List<Int>): Flow<Resource<List<Source>>>
}