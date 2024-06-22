package com.test.application.feature_location.data

import com.test.application.feature_location.domain.model.City
import com.test.application.feature_location.domain.repository.LocationRepository
import javax.inject.Inject

internal class RetrofitRepository @Inject constructor(
    private val locationApi: LocationApi
): LocationRepository {

    override suspend fun getLocationByQuery(query: String): Result<List<City>> {
        return try {
            val chargers = locationApi.getChargers()
            val filteredCities = chargers.filter { info ->
                val cityName = cityMapping[info.city] ?: info.city
                cityName.contains(query, ignoreCase = true)
            }.mapToCitiesWithMapping()
            Result.success(filteredCities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
