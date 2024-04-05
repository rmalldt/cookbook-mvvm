package com.rm.myrecipes.ui.fragments.favourites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rm.myrecipes.R
import com.rm.myrecipes.databinding.FavouritesRowItemLayoutBinding
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.rm.myrecipes.ui.utils.AdapterDiffUtil
import com.rm.myrecipes.ui.utils.loadImage
import com.rm.myrecipes.ui.utils.parseHtml
import com.rm.myrecipes.ui.utils.resetImageViewAndTextViewColor

class FavouriteRecipesAdapter : RecyclerView.Adapter<FavouriteRecipesAdapter.FavouriteRecipesViewHolder>() {

    var recipeList = emptyList<Recipe>()
        set(value) {
            val diffUtil = AdapterDiffUtil(recipeList, value)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffUtilResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteRecipesViewHolder {
        val binding = FavouritesRowItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavouriteRecipesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavouriteRecipesViewHolder,
        position: Int
    ) {
        val recipe = recipeList[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            it.findNavController()
                .navigate(FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsFragment(recipe))
        }
    }

    override fun getItemCount(): Int = recipeList.size

    class FavouriteRecipesViewHolder(val binding: FavouritesRowItemLayoutBinding) :
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