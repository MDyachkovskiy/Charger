package com.test.application.feature_location.di

import android.content.Context
import com.test.application.core.api.location.LocationsApi
import com.test.application.feature_location.presentation.LocationFragment
import dagger.Component

@LocationScope
@Component(
    modules = [LocationModule::class],
    dependencies = [LocationDependenciesProvider::class]
)
abstract class LocationComponent : LocationsApi {

    @Component.Builder
    interface Builder {

        fun locationsDependenciesProvider(
            locationsDependenciesProvider: LocationDependenciesProvider): Builder
        fun build(): LocationComponent
    }

    companion object {
        @Volatile
        private var locationComponent: LocationComponent? = null

        @Synchronized
        fun init(context: Context): LocationComponent {
            if (locationComponent == null) {
                val deps = context.applicationContext as LocationDependenciesProvider
                locationComponent = DaggerLocationComponent
                    .builder()
                    .locationsDependenciesProvider(deps)
                    .build()
            }
            return locationComponent!!
        }
    }

    internal abstract fun injectLocationFragment(locationFragment: LocationFragment)
}