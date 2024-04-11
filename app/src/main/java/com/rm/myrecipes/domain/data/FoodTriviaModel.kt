package com.rm.myrecipes.domain.data

import android.os.Parcelable
import com.rm.myrecipes.ui.utils.EMPTY_STRING
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodTrivia(
    val trivia: String = EMPTY_STRING
) : Parcelable