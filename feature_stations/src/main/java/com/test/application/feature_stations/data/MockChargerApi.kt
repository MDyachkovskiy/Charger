package com.test.application.feature_stations.data

import android.util.Log
import com.test.application.core.model.Charger
import com.test.application.core.model.ChargerInfo
import retrofit2.mock.Calls
import javax.inject.Inject

class MockChargerApi @Inject constructor() : StationsApi {

    override suspend fun getStations(): List<ChargerInfo> {
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

        Log.d("MockChargerApi", "Returning chargers: $chargers")

        return Calls.response(chargers).execute().body() ?: emptyList()
    }
}