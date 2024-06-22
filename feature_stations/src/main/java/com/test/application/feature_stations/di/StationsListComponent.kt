package com.test.application.feature_stations.di

import android.content.Context
import com.test.application.core.api.stations_list.StationsListApi
import com.test.application.feature_stations.presentation.StationsListFragment
import dagger.Component
import javax.inject.Singleton

@StationsListScope
@Component(
    modules = [StationsListModule::class]
)
@Singleton
abstract class StationsListComponent : StationsListApi {

    @Component.Builder
    interface Builder {

        fun build(): StationsListComponent
    }

    companion object {
        @Volatile
        private var stationsListComponent: StationsListComponent? = null

        @Synchronized
        fun init(context: Context): StationsListComponent {
            if (stationsListComponent == null) {
                stationsListComponent = DaggerStationsListComponent
                    .builder()
                    .build()
            }
            return stationsListComponent!!
        }
    }

    internal abstract fun injectStationsFragment(stationsListFragment: StationsListFragment)
}