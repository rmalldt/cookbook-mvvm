package com.rm.myrecipes.ui.fragments.details.ingredients.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rm.myrecipes.R
import com.rm.myrecipes.data.common.ApiConstants
import com.rm.myrecipes.databinding.IngredientsRowItemLayoutBinding
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.ui.utils.common.AdapterDiffUtil
import com.rm.myrecipes.ui.utils.extension.loadImage

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    var ingredientList = emptyList<ExtendedIngredient>()
        set(value) {
            val diffUtil = AdapterDiffUtil(ingredientList, value)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffUtilResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsViewHolder {
        val binding = IngredientsRowItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int = ingredientList.size

    class IngredientsViewHolder(private val binding: IngredientsRowItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(ingredient: ExtendedIngredient) = with(binding) {
                tvIngredientName.text = ingredient.name
                tvAmount.text = ingredient.amount.toString()
                tvUnit.text = ingredient.unit
                tvConsistency.text = ingredient.consistency
                tvOriginal.text = ingredient.original
                imageView.loadImage(
                    "${ApiConstants.INGREDIENT_IMAGE_URL}${ingredient.image}",
                    R.drawable.ic_loading_placeholder,
                    R.drawable.ic_loading_placeholder
                ) {
                    DrawableTransitionOptions.withCrossFade(300)
                }
            }
    }
}