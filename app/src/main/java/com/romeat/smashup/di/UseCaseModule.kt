package com.romeat.smashup.di

import com.romeat.smashup.domain.GetUserInfoUseCase
import com.romeat.smashup.domain.GetUserInfoUseCaseImpl
import com.romeat.smashup.domain.LoginUseCase
import com.romeat.smashup.domain.LoginUseCaseImpl
import com.romeat.smashup.domain.author.GetAuthorUseCase
import com.romeat.smashup.domain.author.GetAuthorUseCaseImpl
import com.romeat.smashup.domain.mashups.*
import com.romeat.smashup.domain.playlists.GetCompilationsUseCase
import com.romeat.smashup.domain.playlists.GetCompilationsUseCaseImpl
import com.romeat.smashup.domain.playlists.GetPlaylistUseCase
import com.romeat.smashup.domain.playlists.GetPlaylistUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindLoginUseCase(useCaseImpl: LoginUseCaseImpl) : LoginUseCase

    @Binds
    abstract fun bindUserInfoUseCase(useCaseImpl: GetUserInfoUseCaseImpl) : GetUserInfoUseCase



    @Binds
    abstract fun bindMashupUseCase(useCaseImpl: GetMashupUseCaseImpl) : GetMashupUseCase

    @Binds
    abstract fun bindMashupsListUseCase(useCaseImpl: GetMashupsListUseCaseImpl) : GetMashupsListUseCase

    @Binds
    abstract fun bindMashupWithSourceUseCase(useCaseImpl: GetMashupsWithSourceUseCaseImpl) : GetMashupsWithSourceUseCase

    @Binds
    abstract fun bindSourceUseCase(useCaseImpl: GetSourceUseCaseImpl) : GetSourceUseCase

    @Binds
    abstract fun bindSourcesListUseCase(useCaseImpl: GetSourcesListUseCaseImpl) : GetSourcesListUseCase

    @Binds
    abstract fun bindAuthorUseCase(useCase: GetAuthorUseCaseImpl) : GetAuthorUseCase



    @Binds
    abstract fun bindCompilationsUseCase(useCaseImpl: GetCompilationsUseCaseImpl) : GetCompilationsUseCase

    @Binds
    abstract fun bindPlaylistUseCase(useCaseImpl: GetPlaylistUseCaseImpl) : GetPlaylistUseCase

}