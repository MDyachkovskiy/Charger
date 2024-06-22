package com.test.application.feature_location.domain.usecase

import com.test.application.feature_location.domain.model.City
import com.test.application.feature_location.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FindLocationUseCaseImpl @Inject constructor (
    private val locationRepository: LocationRepository
) : FindLocationUseCase {
    override suspend fun invoke(query: String): Result<List<City>> =
        withContext(Dispatchers.IO) {
            return@withContext locationRepository.getLocationByQuery(query)
        }
}