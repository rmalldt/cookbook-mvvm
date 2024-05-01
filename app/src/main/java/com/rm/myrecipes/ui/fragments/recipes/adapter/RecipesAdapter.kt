package com.rm.myrecipes.ui.fragments.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.RecipesRowItemLayoutBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.fragments.recipes.RecipesFragmentDirections
import com.rm.myrecipes.ui.utils.AdapterDiffUtil
import com.rm.myrecipes.ui.utils.loadImage
import com.rm.myrecipes.ui.utils.parseHtml
import com.rm.myrecipes.ui.utils.resetImageViewAndTextViewColor

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
        val recipe = recipeList[position]
        holder.bind(recipe)

        holder.binding.recipesRowCardView
            .startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_anim))

        holder.itemView.setOnClickListener {
            it.findNavController()
                .navigate(RecipesFragmentDirections.actionRecipesFragmentToDetailsFragment(recipe))
        }
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(val binding: RecipesRowItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) = with(binding) {
            tvRecipeTitle.text = recipe.title
            tvNoOfLikes.text = recipe.aggregateLikes.toString()
            tvCookingDuration.text = recipe.readyInMinutes.toString()
            ivRecipe.loadImage(recipe.image, R.drawable.ic_loading_placeholder, R.drawable.ic_loading_placeholder) {
                DrawableTransitionOptions().crossFade(700)
            }
            resetImageViewAndTextViewColor(recipe.vegetarian, ivVeg, tvVeg, R.color.green, R.color.green)
            parseHtml(tvRecipeDescription, recipe.summary)
        }
    }
}