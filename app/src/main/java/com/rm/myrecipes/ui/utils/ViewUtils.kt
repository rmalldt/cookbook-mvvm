package com.rm.myrecipes.ui.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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


fun Context.initItemDecorator(): DividerItemDecoration {
    val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider)!!)
    return itemDecorator
}

fun ImageView.loadImage(
    imageUrl: String?,
    @DrawableRes resourceIdForError: Int,
    @DrawableRes resourceIdForPlaceHolder: Int,
    transition: () -> DrawableTransitionOptions = { DrawableTransitionOptions.withCrossFade(0) }
) {
    Glide.with(context)
        .load(imageUrl)
        .error(resourceIdForError)
        .placeholder(resourceIdForPlaceHolder)
        .transition(transition())
        .into(this)
}

fun resetImageViewAndTextViewColor(
    reset: Boolean,
    imageView: ImageView,
    textView: TextView,
    @ColorRes colorIdImageView: Int,
    @ColorRes colorIdTextView: Int
) {
    if (reset) {
        imageView.setImageViewColor(colorIdImageView)
        textView.setTextViewColor(colorIdTextView)
    }
}

fun ImageView.setImageViewColor(@ColorRes colorId: Int) =
    setColorFilter(ContextCompat.getColor(context, colorId ))


fun TextView.setTextViewColor(@ColorRes colorId: Int) =
    setTextColor(ContextCompat.getColor(context, colorId ))

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
