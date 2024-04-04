package com.rm.myrecipes.ui.utils

import android.text.Html
import android.text.Spanned
import android.widget.TextView
import org.jsoup.Jsoup

const val EMPTY_STRING = ""

fun fromHtml(source: String): Spanned = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)

fun logWithThreadAndCoroutineInfo(message: String) = println("[${Thread.currentThread().name}] $message")

fun parseHtml(textView: TextView, description: String?) {
    if (description != null) {
        textView.text = Jsoup.parse(description).text()
    }
}

