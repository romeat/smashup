package com.romeat.smashup.data.likes

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

interface LikesRepository {

    val likesState: StateFlow<LikesState>

    fun addLike(id: Int)
    fun removeLike(id: Int)

    fun updateLikesManually()
}

@Stable
data class LikesState(
    val mashupLikes: Set<Int> = emptySet()
)