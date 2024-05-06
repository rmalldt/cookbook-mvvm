package com.rm.myrecipes

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.rm.myrecipes.ui.MainActivity
import com.rm.myrecipes.ui.fragments.recipes.RecipesFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest                    // mark as Hilt test telling we're injecting Hilt component
class SampleTest {

    /**
     * If there are multiple rule, HiltAndroidRule must run before any other rule.
     * This can be done using order parameter in the Rule annotation as below.
     */
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    /**
     * ActivityScenario runs out of the box that allows to test Activities in isolation.
     */
    @get:Rule(order = 1)
    var activityScenario = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun fragTest() {
         val scenario = launchFragmentInHiltContainer<RecipesFragment>(themeResId = R.style.BaseAppTheme)
    }

}


