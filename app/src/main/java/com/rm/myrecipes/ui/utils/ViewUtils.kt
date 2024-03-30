package com.rm.myrecipes.ui.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.rm.myrecipes.R

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun initItemDecorator(context: Context): DividerItemDecoration {
    val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.recyclerview_divider)!!)
    return itemDecorator
}

fun ImageView.loadImageWithGlide(
    imageUrl: String?,
    @DrawableRes resourceIdForError: Int,
    @DrawableRes resourceIdForPlaceHolder: Int
) {
    Glide.with(context)
        .load(imageUrl)
        .error(resourceIdForError)
        .placeholder(resourceIdForPlaceHolder)
        .into(this)
}

/**
 * From kotlinextensions.com
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {}
    return false
}
