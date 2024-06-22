package com.test.application.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object RouterHolder {
    private var activityRouter: Router? = null
    private val fragmentRouters = mutableMapOf<String, Router>()

    fun getActivityRouter(activity: FragmentActivity, containerId: Int): Router {
        if (activityRouter == null) {
            activityRouter = Router.createRouter(activity, containerId)
        }
        return activityRouter!!
    }

    fun getFragmentRouter(fragment: Fragment, containerId: Int): Router {
        val fragmentTag = fragment::class.java.name
        if (!fragmentRouters.containsKey(fragmentTag)) {
            fragmentRouters[fragmentTag] = Router.createRouter(fragment, containerId)
        }
        return fragmentRouters[fragmentTag]!!
    }

    fun clearRouter() {
        activityRouter = null
    }
}