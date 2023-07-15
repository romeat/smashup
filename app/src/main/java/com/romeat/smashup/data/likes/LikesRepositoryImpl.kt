package com.romeat.smashup.data.likes

import android.util.Log
import com.romeat.smashup.data.LoggedUserRepository
import com.romeat.smashup.domain.mashups.likes.AddLikeUseCase
import com.romeat.smashup.domain.mashups.likes.GetAllLikesUseCase
import com.romeat.smashup.domain.mashups.likes.RemoveLikeUseCase
import com.romeat.smashup.network.SmashupRemoteData
import com.romeat.smashup.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesRepositoryImpl @Inject constructor(
    private val addLikeUseCase: AddLikeUseCase,
    private val removeLikeUseCase: RemoveLikeUseCase,
    private val getAllLikesUseCase: GetAllLikesUseCase,
    private val userRepository: LoggedUserRepository,
) : LikesRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _likesState = MutableStateFlow(LikesState())
    override val likesState = _likesState

    init {
        scope.launch {
            delay(2000)
            userRepository
                .userInfoFlow
                .collect { it?.let{ getUserLikes() } }
        }
    }

    private fun getUserLikes() {
        scope.launch {
            getAllLikesUseCase
                .invoke()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            _likesState.update {
                                it.copy(mashupLikes = result.data!!.toSet())
                            }
                        }
                        else -> { }
                    }
                }
        }
    }

    override fun addLike(id: Int) {
        scope.launch {
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

    override fun removeLike(id: Int) {
        scope.launch {
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
}
