package com.test.application.feature_stations.domain.usecase

import com.test.application.core.model.Charger

internal interface FindStationsUseCase {
    suspend operator fun invoke(city: String): Result<List<Charger>>
}