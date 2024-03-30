package com.rm.myrecipes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application scoped dagger components generated with the annotation.
 */
@HiltAndroidApp
class MyRecipesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        /**
         * Enable Debugging for Kotlin Coroutines in debug builds
         * Prints Coroutine name when logging Thread.currentThread().name
         */
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
    }
}