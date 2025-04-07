package com.uwange.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.databinding.ItemRecipeCardBinding

class RecipeItemAdapter(
    private val callback: (Int, Int) -> Unit
): ListAdapter<RecipeItem, RecipeItemAdapter.RecipeItemViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemAdapter.RecipeItemViewHolder {
        val binding = ItemRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeItemAdapter.RecipeItemViewHolder, position: Int) {
        val recipeItem = getItem(position)
        holder.bind(recipeItem)
    }

    inner class RecipeItemViewHolder(
        private val binding: ItemRecipeCardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipeItem: RecipeItem) {
            setGlideUrlToImage(binding.ivFoodImage, recipeItem.imageUrl)
            binding.ivFoodImage.contentDescription = recipeItem.imageDescription
            binding.tvRecipeTitle.text = recipeItem.name
            binding.tvRecipeReview.text = recipeItem.recipeReview
            binding.tvScore.text = formatScoreAsString(recipeItem.score)
            binding.ivBookmark.visibility = if (recipeItem.bookmark) VISIBLE else GONE

            clickListener(recipeItem.recipeId, recipeItem.foodId)
        }

        private fun clickListener(recipeId:Int, foodId: Int) {
            binding.clFoodCard.setOnClickListener {
                callback(recipeId, foodId)
            }
        }
    }
}

class RecipeDiffCallback: DiffUtil.ItemCallback<RecipeItem>() {
    override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
        oldItem.recipeId == newItem.recipeId

    override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
        oldItem == newItem
}