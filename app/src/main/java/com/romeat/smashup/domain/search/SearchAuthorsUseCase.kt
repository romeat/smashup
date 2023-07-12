package com.romeat.smashup.domain.search

import com.romeat.smashup.data.dto.AuthorProfile
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import com.romeat.smashup.util.getResourceWithExceptionLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class SearchAuthorsUseCase @Inject constructor(
    private val remoteData: SmashupRemoteData
) {
    suspend operator fun invoke(searchQuery: String): Flow<Resource<List<AuthorProfile>>> =
        getResourceWithExceptionLogging(
            dispatcher = Dispatchers.IO,
            action = suspend {
                val response = remoteData.getUsersWithName(searchQuery)
                if (response.isSuccessful) {
                    response.body()!!.response!!
                } else {
                    throw HttpException(response)
                }
            }
        )
}