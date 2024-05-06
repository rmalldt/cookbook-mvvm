package com.rm.myrecipes.ui.fragments.favourites.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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
import com.rm.myrecipes.ui.utils.setGone
import com.rm.myrecipes.ui.utils.setVisible

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val onDeleteClicked: (recipes: List<Recipe>) -> Unit
) : RecyclerView.Adapter<FavouriteRecipesAdapter.FavouriteRecipesViewHolder>(),
    ActionMode.Callback {

    var recipeList = emptyList<Recipe>()
        set(value) {
            val diffUtil = AdapterDiffUtil(recipeList, value)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffUtilResult.dispatchUpdatesTo(this)
        }

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<Recipe>()
    private var viewHolders = arrayListOf<FavouriteRecipesViewHolder>()
    private var mActionMode: ActionMode? = null

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
        viewHolders.add(holder)

        val recipe = recipeList[position]
        holder.bind(recipe)

        holder.itemView.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, recipe)
            } else {
                it.findNavController()
                    .navigate(FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsFragment(recipe))
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                (requireActivity as AppCompatActivity).startSupportActionMode(this)
                applySelection(holder, recipe)
                true
            } else {
                // When multiSelection is already true, setting multiSelection as false here
                // will result navigating to DetailsFragment with the ActionMode not destroyed
                // because long click subsequently calls above setOnClickListener with false
                // value if multiSelection is set to false here.
                // multiSelection is reset to false onDestroyActionMode.
                false
            }
        }
    }

    override fun getItemCount(): Int = recipeList.size

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favourites_fragment_menu, menu)
        mActionMode = actionMode
        setStatusBarColor(R.color.colorStatusBarActionMode)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_delete_favourite) {
            onDeleteClicked(selectedRecipes)
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        multiSelection = false
        selectedRecipes.clear()
        viewHolders.forEach {  viewHolder ->
            changeViewHolderOnSelection(viewHolder, false)
        }
        setStatusBarColor(R.color.colorStatusBar)
    }

    private fun applySelection(holder: FavouriteRecipesViewHolder, currentRecipe: Recipe) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeViewHolderOnSelection(holder, false)
        } else {
            selectedRecipes.add(currentRecipe)
            changeViewHolderOnSelection(holder, true)
        }
        setActionModeTitle()
    }

    private fun changeViewHolderOnSelection(holder: FavouriteRecipesViewHolder, selected: Boolean) {
        if (selected) {
            holder.binding.ivGradientOverlay.setVisible()
            holder.binding.recipesRowCardView.strokeColor = ContextCompat.getColor(requireActivity, R.color.colorPrimary)
        } else {
            holder.binding.ivGradientOverlay.setGone()
            holder.binding.recipesRowCardView.strokeColor = ContextCompat.getColor(requireActivity, R.color.colorStroke)
        }
    }

    private fun setActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> mActionMode?.finish()
            1 -> mActionMode?.title = "${selectedRecipes.size} item selected"
            else -> mActionMode?.title = "${selectedRecipes.size} items selected"
        }
    }

    private fun setStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun clearContextualActionMode() {
        if (mActionMode != null) {
            mActionMode?.finish()
        }
    }
}