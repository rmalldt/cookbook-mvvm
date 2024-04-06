package com.rm.myrecipes.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodTrivia(
    val trivia: String
) : Parcelable