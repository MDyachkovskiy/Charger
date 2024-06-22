package com.test.application.feature_location.di

import com.test.application.feature_location.data.LocationApi
import com.test.application.feature_location.data.MockLocationApi
import dagger.Binds
import dagger.Module

@Module(includes = [ApiModule::class, UseCaseModule::class])
internal abstract class LocationModule {
    @Binds
    @LocationScope
    abstract fun bindLocationApi(mockLocationApi: MockLocationApi): LocationApi
}