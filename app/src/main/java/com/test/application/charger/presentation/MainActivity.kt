package com.test.application.charger.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.application.charger.R
import com.test.application.charger.app.ChargerApplication
import com.test.application.core.navigation.RouteProvider
import com.test.application.core.navigation.Router
import com.test.application.core.navigation.RouterHolder
import com.test.application.core.api.location.LocationProvider
import javax.inject.Inject

class MainActivity : AppCompatActivity(), RouteProvider {

    @Inject
    lateinit var locationProvider: LocationProvider

    override val router: Router
        get() = RouterHolder.getActivityRouter(this, R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as ChargerApplication).appComponent.inject(this)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            router.replaceScreen(locationProvider.serviceConnectedCities())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RouterHolder.clearRouter()
    }
}