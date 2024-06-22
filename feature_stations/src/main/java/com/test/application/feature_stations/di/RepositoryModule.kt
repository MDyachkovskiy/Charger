package com.test.application.feature_stations.di

import com.test.application.feature_stations.data.RetrofitRepository
import com.test.application.feature_stations.domain.repository.StationsRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryModule {

    @StationsListScope
    @Binds
    abstract fun bindStationsRepository(
        repository: RetrofitRepository
    ): StationsRepository
}