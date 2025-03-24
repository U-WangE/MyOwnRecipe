package com.uwnage.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uwnage.myownrecipe.R
import com.uwnage.myownrecipe.data.FoodItem
import com.uwnage.myownrecipe.databinding.ItemFoodCardBinding
import java.util.Locale

class FoodItemAdapter(
    private val foodList: List<FoodItem>,
    private val callback: (Int) -> Unit
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
            setGlideUrlToImage(foodItem.imageUrl)
            binding.tvHeaderText.text = foodItem.name
            binding.tvSupportText.text = foodItem.supportText
            binding.tvScore.text = String.format(Locale.getDefault(), "%0.2f", foodItem.score)

            clickListener(foodItem.id)
        }

        private fun setGlideUrlToImage(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl) // foodItem 객체에 이미지 URL이 있다고 가정
                .placeholder(R.drawable.placeholder_image) // 로딩 중 표시할 플레이스홀더 이미지
                .error(R.drawable.placeholder_image) // 로딩 실패 시 표시할 이미지
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐싱 전략 설정
                .into(binding.ivFoodImage) // 이미지 뷰에 로드
        }

        private fun clickListener(id: Int) {
            binding.clFoodCard.setOnClickListener {
                callback(id)
            }
        }
    }
}
