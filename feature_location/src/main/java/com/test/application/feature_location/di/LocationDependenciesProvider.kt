package com.test.application.feature_location.di

import com.test.application.core.api.stations_list.StationsListProvider

interface LocationDependenciesProvider {
    fun getStationsListProvider(): StationsListProvider
}