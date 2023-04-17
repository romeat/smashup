package com.romeat.smashup.di

import com.romeat.smashup.data.likes.LikesRepository
import com.romeat.smashup.data.likes.LikesRepositoryImpl
import com.romeat.smashup.data.likes.UserLikesHolder
import com.romeat.smashup.domain.mashups.likes.AddLikeUseCase
import com.romeat.smashup.domain.mashups.likes.AddLikeUseCaseImpl
import com.romeat.smashup.domain.mashups.likes.RemoveLikeUseCase
import com.romeat.smashup.domain.mashups.likes.RemoveLikeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LikesModule {

    @Binds
    abstract fun bindLikesRepository(repository: LikesRepositoryImpl) : LikesRepository

    @Binds
    abstract fun bindUserLikesHolder(repository: LikesRepositoryImpl) : UserLikesHolder

    @Binds
    abstract fun bindAddLikeUseCase(useCase: AddLikeUseCaseImpl) : AddLikeUseCase

    @Binds
    abstract fun bindRemoveLikeUseCase(useCase: RemoveLikeUseCaseImpl) : RemoveLikeUseCase
}