package com.test.application.feature_location.di

import androidx.fragment.app.Fragment
import com.test.application.core.api.location.LocationProvider
import com.test.application.feature_location.presentation.LocationFragment
import javax.inject.Inject

internal class LocationProviderImpl @Inject constructor() : LocationProvider {
    override fun serviceConnectedCities(): Fragment {
        return LocationFragment()
    }
}