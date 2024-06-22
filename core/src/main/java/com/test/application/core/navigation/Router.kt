package com.test.application.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class Router(
    private val activity: FragmentActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {
    companion object {
        fun createRouter(fragment: Fragment, containerId: Int): Router {
            return Router(fragment.requireActivity(),fragment.childFragmentManager,
                containerId)
        }

        fun createRouter(activity: FragmentActivity, containerId: Int): Router {
            return Router(activity, activity.supportFragmentManager, containerId)
        }
    }

    fun navigateTo(
        fragment: Fragment,
        addToBackStack: Boolean = true,
    ) {
        if(!fragmentManager.isStateSaved) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(containerId, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }

    fun replaceScreen(fragment: Fragment, addToBackStack: Boolean = false) {
        if(!fragmentManager.isStateSaved) {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }

    fun exit() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            activity.finish()
        }
    }
}