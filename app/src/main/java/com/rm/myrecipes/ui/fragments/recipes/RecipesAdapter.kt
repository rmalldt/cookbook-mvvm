package com.rm.myrecipes.ui.fragments.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.RecipesRowItemLayoutBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.utils.AdapterDiffUtil
import com.rm.myrecipes.ui.utils.loadImageWithGlide

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    var recipeList = emptyList<Recipe>()
        set(value) {
            val recipeDiffUtil = AdapterDiffUtil(recipeList, value)
            val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
            field = value
            diffUtilResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipesRowItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList.get(position)
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(private val binding: RecipesRowItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) = with(binding) {
            tvRecipeTitle.text = recipe.title
            tvRecipeDescription.text = recipe.summary
            tvNoOfLikes.text = recipe.aggregateLikes.toString()
            tvCookingDuration.text = recipe.readyInMinutes.toString()
            setVeganImageViewAndTextColour(ivVeg, recipe.vegetarian)
            setVeganImageViewAndTextColour(tvVeg, recipe.vegetarian)
            loadImage(ivRecipe, recipe.image)
        }

        private fun loadImage(
            imageView: ImageView,
            imageUrl: String
        ) = imageView.loadImageWithGlide(
            imageUrl,
            R.drawable.ic_loading_placeholder,
            R.drawable.ic_loading_placeholder
        )

        private fun setVeganImageViewAndTextColour(view: View, vegan: Boolean ) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }
    }
}