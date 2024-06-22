package com.test.application.feature_location.domain.repository

import com.test.application.feature_location.domain.model.City

internal interface LocationRepository {
    suspend fun getLocationByQuery(query: String): Result<List<City>>
}