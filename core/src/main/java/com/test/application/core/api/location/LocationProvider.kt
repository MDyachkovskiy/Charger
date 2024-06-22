package com.test.application.core.api.location

import androidx.fragment.app.Fragment

interface LocationProvider {
    fun serviceConnectedCities(): Fragment
}