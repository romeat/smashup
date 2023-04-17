package com.romeat.smashup.data.likes

import kotlinx.coroutines.flow.StateFlow

interface LikesRepository {

    val likesState: StateFlow<LikesState>

    fun addLike(id: Int)
    fun removeLike(id: Int)
}


data class LikesState(
    val mashupLikes: Set<Int> = emptySet()
)