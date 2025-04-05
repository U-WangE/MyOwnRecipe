package com.uwange.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.databinding.ItemRecipeCardBinding
import java.util.Locale

class RecipeItemAdapter(
    private val recipeList: List<RecipeItem>,
    private val callback: (Int, String) -> Unit
): RecyclerView.Adapter<RecipeItemAdapter.RecipeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): RecipeItemAdapter.RecipeItemViewHolder {
        val binding = ItemRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeItemViewHolder(binding)
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecipeItemAdapter.RecipeItemViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    inner class RecipeItemViewHolder(private val binding: ItemRecipeCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipeItem: RecipeItem) {
            setGlideUrlToImage(binding.ivFoodImage, recipeItem.imageUrl)
            binding.ivFoodImage.contentDescription = recipeItem.imageDescription
            binding.tvRecipeTitle.text = recipeItem.name
            binding.tvRecipeReview.text = recipeItem.recipeReview
            binding.tvScore.text = formatScoreAsString(recipeItem.score)
            binding.ivBookmark.visibility = if (recipeItem.bookmark) VISIBLE else GONE

            clickListener(recipeItem.id, recipeItem.name)
        }

        private fun clickListener(id: Int, name: String) {
            binding.clFoodCard.setOnClickListener {
                callback(id, name)
            }
        }
    }
}