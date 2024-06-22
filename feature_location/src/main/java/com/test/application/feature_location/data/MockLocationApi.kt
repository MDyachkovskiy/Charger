package com.test.application.feature_location.data

import com.test.application.core.model.Charger
import com.test.application.core.model.ChargerInfo
import retrofit2.mock.Calls
import javax.inject.Inject

class MockLocationApi @Inject constructor(): LocationApi {
    override suspend fun getChargers(): List<ChargerInfo> {
        val chargers = listOf(
            ChargerInfo(
                "Moscow",
                Charger(
                    true,
                    "Энергия Москвы",
                    "Измайловское ш., 4А"
                )
            ),
            ChargerInfo(
                "Moscow",
                Charger(
                    false,
                    "Lipgart",
                    "2-я Бауманская ул., 5, стр. 5"
                )
            ),
            ChargerInfo(
                "Saint Petersburg",
                Charger(
                    true,
                    "Станция зарядки электромобилей",
                    "Большой просп. Васильевского острова, 68"
                )
            ),
            ChargerInfo(
                "Moscow",
                Charger(
                    false,
                    "Zevs",
                    "Мясницкая ул., 13, стр. 10"
                )
            ),
            ChargerInfo(
                "Saint Petersburg",
                Charger(
                    false,
                    "Punkt E",
                    "Малая Монетная ул., 2Г"
                )
            )
        )

        return Calls.response(chargers).execute().body() ?: emptyList()
    }
}