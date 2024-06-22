package com.test.application.core.navigation

import androidx.fragment.app.FragmentActivity

object RouterHolder {
    private var activityRouter: Router? = null

    fun getActivityRouter(activity: FragmentActivity, containerId: Int): Router {
        if (activityRouter == null) {
            activityRouter = Router.createRouter(activity, containerId)
        }
        return activityRouter!!
    }
    fun clearRouter() {
        activityRouter = null
    }
}