package com.romeat.smashup.domain.mashups

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMashupUseCaseImpl @Inject constructor(
    private val getMashupsListUseCase: GetMashupsListUseCase
) : GetMashupUseCase {
    override suspend fun invoke(mashupId: Int): Flow<Resource<List<Mashup>>> =
        getMashupsListUseCase.invoke(listOf(mashupId))
}