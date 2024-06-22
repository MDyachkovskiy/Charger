package com.test.application.charger.di

import android.content.Context
import com.test.application.charger.presentation.MainActivity
import com.test.application.core.api.location.LocationsApi
import com.test.application.core.api.stations_list.StationsListApi
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [
    LocationsApi::class, StationsListApi::class
])
abstract class AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun locationsApi(deps: LocationsApi): Builder
        fun stationsListApi(deps: StationsListApi): Builder
        fun build(): AppComponent
    }

    abstract fun inject(activity: MainActivity)
}