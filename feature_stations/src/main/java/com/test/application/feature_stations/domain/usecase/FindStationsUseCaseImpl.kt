package com.test.application.feature_stations.domain.usecase

import com.test.application.core.model.Charger
import com.test.application.feature_stations.domain.repository.StationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FindStationsUseCaseImpl @Inject constructor(
    private val stationsRepository: StationsRepository
): FindStationsUseCase {
    override suspend fun invoke(city: String): Result<List<Charger>> =
        withContext(Dispatchers.IO) {
            val originalCity = reverseCityMapping[city] ?: city
            return@withContext stationsRepository.getStationsByCity(originalCity)
        }

    val reverseCityMapping = mapOf(
        "Москва" to "Moscow",
        "Санкт-Петербург" to "Saint Petersburg"
    )
}