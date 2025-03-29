package com.uwange.myownrecipe.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uwange.myownrecipe.R
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
            setGlideUrlToImage(recipeItem.imageUrl)
            binding.tvHeaderText.text = recipeItem.name
            binding.tvSupportText.text = recipeItem.supportText
            binding.tvScore.text = String.format(Locale.getDefault(), "%.2f", recipeItem.score)
            binding.ivBookmark.visibility = if (recipeItem.bookmark) VISIBLE else GONE
            clickListener(recipeItem.id, recipeItem.name)
        }

        private fun setGlideUrlToImage(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl) // foodItem 객체에 이미지 URL이 있다고 가정
                .placeholder(R.drawable.placeholder_image) // 로딩 중 표시할 플레이스홀더 이미지
                .error(R.drawable.placeholder_image) // 로딩 실패 시 표시할 이미지
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐싱 전략 설정
                .into(binding.ivFoodImage) // 이미지 뷰에 로드
        }

        private fun clickListener(id: Int, name: String) {
            binding.clFoodCard.setOnClickListener {
                callback(id, name)
            }
        }
    }
}