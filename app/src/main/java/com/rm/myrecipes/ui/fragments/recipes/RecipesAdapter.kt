package com.rm.myrecipes.ui.fragments.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.RecipesRowItemLayoutBinding
import com.rm.myrecipes.domain.data.Recipe

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    var recipeList = emptyList<Recipe>()
        set(value) {
            val recipeDiffUtil = RecipeDiffUtil(recipeList, value)
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
            setVeganColour(ivVeg, recipe.vegetarian)
            setVeganColour(tvVeg, recipe.vegetarian)
            loadImage(ivRecipe, recipe.image)
        }

        private fun loadImage(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .error(R.drawable.ic_loading_placeholder)
                .placeholder(R.drawable.ic_loading_placeholder)
                .into(imageView)
        }

        private fun setVeganColour(view: View, vegan: Boolean ) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(view.context, R.color.green)
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(view.context, R.color.green)
                        )
                    }
                }
            }
        }
    }

    class RecipeDiffUtil(
        private val oldList: List<Recipe>,
        private val newList: List<Recipe>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}