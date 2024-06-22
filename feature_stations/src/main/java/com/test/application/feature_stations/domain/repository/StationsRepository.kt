package com.test.application.feature_stations.domain.repository

import com.test.application.core.model.Charger

internal interface StationsRepository {
    suspend fun getStationsByCity(city: String): Result<List<Charger>>
}