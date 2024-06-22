package com.test.application.feature_location.data

import com.test.application.core.model.ChargerInfo
import retrofit2.http.GET

interface LocationApi {
    @GET("locations")
    suspend fun getChargers(): List<ChargerInfo>
}