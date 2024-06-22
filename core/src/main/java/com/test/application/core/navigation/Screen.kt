package com.test.application.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

interface Screen {
    fun createFragment(factory: FragmentFactory): Fragment
}