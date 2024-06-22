package com.test.application.feature_location.domain.usecase

import com.test.application.feature_location.domain.model.City

internal interface FindLocationUseCase {
    suspend operator fun invoke(query: String): Result<List<City>>
}