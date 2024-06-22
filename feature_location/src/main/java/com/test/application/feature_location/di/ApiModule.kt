package com.test.application.feature_location.di

import com.test.application.core.api.location.LocationProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ApiModule {
    @LocationScope
    @Binds
    abstract fun bindLocationProvider(
        locationsProviderImpl: LocationProviderImpl
    ): LocationProvider
}