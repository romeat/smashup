package com.romeat.smashup.data.likes

import com.romeat.smashup.domain.mashups.likes.AddLikeUseCase
import com.romeat.smashup.domain.mashups.likes.RemoveLikeUseCase
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesRepositoryImpl @Inject constructor(
    private val addLikeUseCase: AddLikeUseCase,
    private val removeLikeUseCase: RemoveLikeUseCase
) : LikesRepository, UserLikesHolder {

    private val _likesState = MutableStateFlow(LikesState())
    override val likesState = _likesState

    @OptIn(DelicateCoroutinesApi::class)
    override fun addLike(id: Int) {
        GlobalScope.launch {
            addLikeUseCase
                .invoke(id)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            _likesState.update {
                                val currentSet = it.mashupLikes.toMutableSet()
                                it.copy(mashupLikes = currentSet + id)
                            }
                        }
                        else -> { /* TODO show toast in case of error? */ }
                    }
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun removeLike(id: Int) {
        GlobalScope.launch {
            removeLikeUseCase
                .invoke(id)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            _likesState.update {
                                val currentSet = it.mashupLikes.toMutableSet()
                                it.copy(mashupLikes = currentSet - id)
                            }
                        }
                        else -> { }
                    }
                }
        }
    }

    override fun updateLoggedUserLikes(likes: List<Int>) {
        _likesState.value = LikesState(mashupLikes = likes.toSet())
    }
}
