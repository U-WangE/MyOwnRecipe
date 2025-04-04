package com.uwange.myownrecipe

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.Locale

object Util {
    fun setGlideUrlToImage(imageView: ImageView, imageUrl: String) {
        // Image View 용해야 해당 View 가 속한 Context 의 Lifecycle을 따름
        Glide.with(imageView)
            .load(imageUrl) // foodItem 객체에 이미지 URL이 있다고 가정
            .placeholder(R.drawable.placeholder_image) // 로딩 중 표시할 플레이스홀더 이미지
            .error(R.drawable.placeholder_image) // 로딩 실패 시 표시할 이미지
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐싱 전략 설정
            .into(imageView) // 이미지 뷰에 로드
    }

    fun formatScoreAsString(score: Float): String {
        return String.format(Locale.getDefault(), "%.2f", score)
    }
}