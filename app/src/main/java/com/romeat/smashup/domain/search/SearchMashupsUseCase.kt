package com.romeat.smashup.domain.search

import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.presentation.home.search.SearchResult
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class SearchMashupsUseCase @Inject constructor(
    private val remoteData: SmashupRemoteData
) : SearchUseCase {
    override suspend operator fun invoke(searchQuery: String): Flow<Resource<SearchResult>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getMashupsWithName(searchQuery)
                if (response.isSuccessful) {
                    SearchResult.Mashups(response.body()!!)
                } else {
                    throw HttpException(response)
                }
            }
        )
}