package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Source
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSourceUseCaseImpl @Inject constructor(
    private val getSourcesListUseCase: GetSourcesListUseCase
) : GetSourceUseCase {
    override suspend fun invoke(sourceId: Int): Flow<Resource<List<Source>>> =
        getSourcesListUseCase.invoke(listOf(sourceId))
}