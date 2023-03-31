package com.romeat.smashup.domain.search

import com.romeat.smashup.presentation.home.search.SearchResult
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    suspend operator fun invoke(searchQuery: String): Flow<Resource<SearchResult>>
}