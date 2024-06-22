package com.test.application.feature_stations.di

import com.test.application.feature_stations.domain.usecase.FindStationsUseCase
import com.test.application.feature_stations.domain.usecase.FindStationsUseCaseImpl
import dagger.Binds
import dagger.Module

@Module(includes = [RepositoryModule::class])
internal abstract class UseCaseModule {

    @StationsListScope
    @Binds
    abstract fun bindFindStationsUseCase(
        findStationsUseCase: FindStationsUseCaseImpl
    ): FindStationsUseCase
}