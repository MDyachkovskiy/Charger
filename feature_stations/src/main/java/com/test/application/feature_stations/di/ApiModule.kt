package com.test.application.feature_stations.di

import com.test.application.core.api.stations_list.StationsListProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ApiModule {

    @StationsListScope
    @Binds
    abstract fun bindStationsListProvider(
        stationsListProviderImpl: StationsListProviderImpl
    ): StationsListProvider
}