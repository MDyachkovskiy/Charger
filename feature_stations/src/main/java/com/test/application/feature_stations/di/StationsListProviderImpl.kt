package com.test.application.feature_stations.di

import androidx.fragment.app.Fragment
import com.test.application.core.api.stations_list.StationsListProvider
import com.test.application.feature_stations.presentation.StationsListFragment
import javax.inject.Inject

internal class StationsListProviderImpl @Inject constructor(): StationsListProvider {
    override fun stationsListByCity(city: String): Fragment {
        return StationsListFragment.newInstance(city)
    }
}