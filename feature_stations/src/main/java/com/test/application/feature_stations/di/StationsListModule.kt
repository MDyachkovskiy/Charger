package com.test.application.feature_stations.di

import com.test.application.feature_stations.data.MockChargerApi
import com.test.application.feature_stations.data.StationsApi
import dagger.Binds
import dagger.Module

@Module(includes = [ApiModule::class, UseCaseModule::class])
internal abstract class StationsListModule {

    @Binds
    @StationsListScope
    abstract fun bindStationsApi(mockLocationApi: MockChargerApi): StationsApi
}