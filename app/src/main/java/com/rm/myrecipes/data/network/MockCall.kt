package com.rm.myrecipes.data.network

import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.Recipes
import kotlinx.coroutines.delay
import timber.log.Timber

object MockCall {
    suspend fun fetchDummyRemote(): Recipes {
        Timber.tag("Recipe").d("FETCHING FROM NETWORK...")
        delay(800)
        return Recipes(listOf(
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", null, "name", "original", "unit")
                ),
                true,
                1,
                "https://img.spoonacular.com/recipes/782585-312x231.jpg",
                20,
                "sourceName",
                "http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html",
                "summary",
                "title",
                true,
                true,
                true
            ),
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", null, "name", "original", "unit")
                ),
                true,
                1,
                "https://img.spoonacular.com/recipes/715415-312x231.jpg",
                20,
                "sourceName",
                "http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html",
                "summary",
                "title",
                true,
                true,
                true
            ),
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", null, "name", "original", "unit")
                ),
                true,
                1,
                "https://img.spoonacular.com/recipes/715446-312x231.jpg",
                20,
                "sourceName",
                "http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html",
                "summary",
                "title",
                true,
                true,
                true
            ),
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", null, "name", "original", "unit")
                ),
                true,
                1,
                "https://img.spoonacular.com/recipes/782601-312x231.jpg",
                20,
                "sourceName",
                "http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html",
                "summary",
                "title",
                true,
                true,
                true
            ),
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", null, "name", "original", "unit")
                ),
                true,
                1,
                "https://img.spoonacular.com/recipes/795751-312x231.jpg",
                20,
                "sourceName",
                "http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html",
                "summary",
                "title",
                true,
                true,
                true
            )
        ))
    }
}