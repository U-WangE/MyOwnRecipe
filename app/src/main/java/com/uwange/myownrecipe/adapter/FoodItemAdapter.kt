package com.uwange.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.FoodItem
import com.uwange.myownrecipe.databinding.ItemFoodCardBinding

class FoodItemAdapter(
    private val foodList: List<FoodItem>,
    private val callback: (Int, String) -> Unit
): RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding = ItemFoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    inner class FoodItemViewHolder(private val binding: ItemFoodCardBinding): RecyclerView.ViewHolder(binding.root) {
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
                callback(id, name)
            }
        }
    }
}
