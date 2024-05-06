package com.rm.myrecipes.util

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltTestApplication
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

object FileReader {

    fun readStringFromFile(filename: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext as HiltTestApplication).assets.open(filename)
            val sb = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                sb.append(it)
            }
            return sb.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}