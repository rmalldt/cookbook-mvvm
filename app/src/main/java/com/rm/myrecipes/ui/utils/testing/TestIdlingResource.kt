package com.rm.myrecipes.ui.utils.testing

import com.rm.myrecipes.BuildConfig

object TestIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = SimpleIdlingResource(RESOURCE)

    fun increment() {
        if (BuildConfig.DEBUG) {
            countingIdlingResource.increment()
        }
    }

    fun decrement() {
        if (BuildConfig.DEBUG) {
            if (!countingIdlingResource.isIdleNow) {
                countingIdlingResource.decrement()
            }
        }
    }
}