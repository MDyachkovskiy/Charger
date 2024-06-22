package com.test.application.feature_stations.data

import com.test.application.core.model.Charger
import com.test.application.feature_stations.domain.repository.StationsRepository
import javax.inject.Inject

internal class RetrofitRepository @Inject constructor(
    private val stationsApi: StationsApi
) : StationsRepository {

    override suspend fun getStationsByCity(city: String): Result<List<Charger>> {
        return try {
            val chargerInfoList = stationsApi.getStations()
            val stations = chargerInfoList.filter { it.city == city }
                .map {
                Charger(busy= it.charger.busy, name = it.charger.name, address = it.charger.address)
            }
            Result.success(stations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}