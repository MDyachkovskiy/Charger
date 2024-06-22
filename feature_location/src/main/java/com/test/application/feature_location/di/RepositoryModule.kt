package com.test.application.feature_location.di

import com.test.application.feature_location.data.RetrofitRepository
import com.test.application.feature_location.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryModule {
    @LocationScope
    @Binds
    abstract fun bindLocationRepository(
        repository: RetrofitRepository
    ): LocationRepository
}