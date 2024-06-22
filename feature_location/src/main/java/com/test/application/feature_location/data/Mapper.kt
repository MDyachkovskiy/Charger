package com.test.application.feature_location.data

import com.test.application.core.model.ChargerInfo
import com.test.application.feature_location.domain.model.City

fun List<ChargerInfo>.mapToCitiesWithMapping(): List<City> {
    val cityCounts = mutableMapOf<String, Int>()
    for (info in this) {
        val cityName = cityMapping[info.city] ?: info.city
        cityCounts[cityName] = cityCounts.getOrDefault(cityName, 0) + 1
    }
    return cityCounts.entries.mapIndexed { index, entry ->
        City(index, entry.key, entry.value)
    }
}

val cityMapping = mapOf(
    "Moscow" to "Москва",
    "Saint Petersburg" to "Санкт-Петербург"
)