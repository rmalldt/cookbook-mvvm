package com.rm.myrecipes.data.network

import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.RecipeResult
import kotlinx.coroutines.delay
import timber.log.Timber

object MockCall {
    suspend fun fetchDummyRemote(): RecipeResult {
        Timber.d("Recipe: FETCHING FROM NETWORK...")
        delay(800)
        return RecipeResult(listOf(
            Recipe(
                100,
                false,
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
                "Cannellini Bean and Asparagus Salad with Mushrooms requires approximately <b>45 minutes</b> " +
                        "from start to finish. This main course has <b>482 calories</b>, <b>31g of protein</b>, and <b>6g of fat</b> per serving. " +
                        "This gluten free, dairy free, lacto ovo vegetarian, and vegan recipe serves 6 and costs <b>$1.35 per serving</b>. " +
                        "309 people were impressed by this recipe. Head to the store and pick up grain mustard, sea salt, lemon zest, and a few other things to make it today. " +
                        "It is brought to you by foodandspice.blogspot.com. " +
                        "Taking all factors into account, this recipe <b>earns a spoonacular score of 70%</b>, which is pretty good. " +
                        "Similar recipes are <a href=\"https://spoonacular.com/recipes/cannellini-bean-salad-422994\">" +
                        "Cannellini Bean Salad</a>, <a href=\"https://spoonacular.com/recipes/refreshing-cannellini-bean-salad-113127\">" +
                        "Refreshing Cannellini Bean Salad</a>, and <a href=\"https://spoonacular.com/recipes/cannellini-and-green-bean-salad-33177\">" +
                        "Cannellini-and-Green Bean Salad</a>." +
                        "This gluten free, dairy free, lacto ovo vegetarian, and vegan recipe serves 6 and costs <b>$1.35 per serving</b>. " +
                        "309 people were impressed by this recipe. Head to the store and pick up grain mustard, sea salt, lemon zest, and a few other things to make it today. " +
                        "It is brought to you by foodandspice.blogspot.com.",
                "Cannellini Bean and Asparagus Salad with Mushrooms",
                true,
                true,
                true
            ),
            Recipe(
                100,
                true,
                true,
                listOf(
                    ExtendedIngredient(12.2, "SOL", "apple", "name", "original", "unit")
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

    suspend fun fetchDummySearch(): RecipeResult {
        Timber.d("Recipe: FETCHING FROM NETWORK...")
        delay(800)
        return RecipeResult(listOf(
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