package com.test.application.feature_stations.data

import com.test.application.core.model.ChargerInfo
import retrofit2.http.GET

internal interface StationsApi {
    @GET("stations")
    suspend fun getStations(): List<ChargerInfo>
}