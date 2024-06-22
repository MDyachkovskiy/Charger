package com.test.application.core.api.stations_list

import androidx.fragment.app.Fragment

interface StationsListProvider {
    fun stationsListByCity(city: String): Fragment
}