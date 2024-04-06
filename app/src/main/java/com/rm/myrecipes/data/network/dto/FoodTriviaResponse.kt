package com.rm.myrecipes.data.network.dto

import com.google.gson.annotations.SerializedName

data class FoodTriviaResponse(
    @SerializedName("text") val trivia: String
)