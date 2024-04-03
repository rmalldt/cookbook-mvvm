package com.rm.myrecipes.ui.utils

import android.text.Html
import android.text.Spanned

fun fromHtml(source: String): Spanned = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)

fun logWithThreadAndCoroutineInfo(message: String) = println("[${Thread.currentThread().name}] $message")

const val EMPTY_STRING = ""
