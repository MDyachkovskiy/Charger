package com.test.application.charger.app

import android.app.Application
import com.test.application.charger.di.AppComponent
import com.test.application.charger.di.DaggerAppComponent
import com.test.application.core.api.stations_list.StationsListProvider
import com.test.application.feature_location.di.LocationComponent
import com.test.application.feature_location.di.LocationDependenciesProvider
import com.test.application.feature_stations.di.StationsListComponent

class ChargerApplication :
    Application(), LocationDependenciesProvider {

    lateinit var appComponent: AppComponent
    lateinit var locationComponent: LocationComponent
    lateinit var stationsListComponent: StationsListComponent

    override fun onCreate() {
        super.onCreate()
        locationComponent = LocationComponent.init(this)
        stationsListComponent = StationsListComponent.init(this)

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .locationsApi(locationComponent)
            .stationsListApi(stationsListComponent)
            .build()
    }

    override fun getStationsListProvider(): StationsListProvider {
        return stationsListComponent.stationsListProvider()
    }
}