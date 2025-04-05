package com.uwange.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.databinding.ItemFoodCardBinding

class FoodItemAdapter(
    private val onItemClick: (Int, String) -> Unit
): ListAdapter<FoodItem, FoodItemAdapter.FoodItemViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding = ItemFoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }

    inner class FoodItemViewHolder(
        private val binding: ItemFoodCardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            setGlideUrlToImage(binding.ivFoodImage, foodItem.imageUrl)
            binding.ivFoodImage.contentDescription = foodItem.imageDescription
            binding.tvFoodTitle.text = foodItem.name
            binding.tvRecipeReview.text = foodItem.recipeReview
            binding.tvScore.text = formatScoreAsString(foodItem.score)

            clickListener(foodItem.foodId, foodItem.name)
        }

        private fun clickListener(id: Int, name: String) {
            binding.clFoodCard.setOnClickListener {
                onItemClick(id, name)
            }
        }
    }
}

class FoodDiffCallback: DiffUtil.ItemCallback<FoodItem>() {
    override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean =
        oldItem.foodId == newItem.foodId

    override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean =
        oldItem == newItem
}
