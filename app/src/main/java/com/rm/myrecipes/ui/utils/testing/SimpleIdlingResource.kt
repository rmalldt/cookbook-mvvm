package com.rm.myrecipes.ui.utils.testing

import androidx.test.espresso.IdlingResource

import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.Volatile

class SimpleIdlingResource(
    private val resourceName: String
) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallBack: IdlingResource.ResourceCallback? = null

    override fun getName(): String = resourceName

    override fun isIdleNow(): Boolean = counter.get() == 0

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallBack = resourceCallback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            resourceCallBack?.onTransitionToIdle()
        } else if (counterVal < 0) {
            throw IllegalStateException("Counter has been corrupted")
        }
    }







}