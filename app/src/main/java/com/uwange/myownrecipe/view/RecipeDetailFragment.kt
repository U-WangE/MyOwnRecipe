package com.uwange.myownrecipe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentRecipeDetailBinding
import com.uwange.myownrecipe.viewModel.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding:FragmentRecipeDetailBinding get() = _binding!!
    private lateinit var viewModel: RecipeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RecipeDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ResponseForm.Loading ->{

                        }
                        is ResponseForm.Success -> {
                            uiSetting()
                        }
                        is ResponseForm.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun uiSetting() {
        viewModel.getRecipe()?.let {
            clickListener(it.recipeId, it.foodId)

            // Set Recipe Image
            setGlideUrlToImage(binding.ivFoodImage, it.imageUrl)
            binding.ivFoodImage.contentDescription = it.imageDescription
            // Set Bookmark State
            setBookmarkView(it.bookmark)

            binding.tvFoodName.text = viewModel.getFoodName()
            binding.tvScore.text = formatScoreAsString(it.score)
            binding.tvRecipeTitle.text = it.recipeName
            binding.tvRecipeSteps.text = it.recipeSteps
            binding.tvIngredients.text = it.ingredients
            binding.tvRecipeReview.text = it.recipeReview
        }
    }

    private fun setBookmarkView(isBookmarked: Boolean) = with(binding.ivBookmark) {
        //TODO:: Bookmark 변경시 Repo에 적용
        setBackgroundResource(
            if (isBookmarked) R.drawable.ic_bookmark_24 else R.drawable.ic_bookmark_border_24
        )
        backgroundTintList = resources.getColorStateList(
            if (isBookmarked) R.color.yellow_600 else R.color.gray_300,
            null
        )

        //TODO Back Button 처리
    }

    private fun clickListener(recipeId: Int?, foodId: Int?) {
        binding.tvEditBtn.setOnClickListener {
            viewModel.savedRecipeEditorArgumentData(RecipeArgumentData(recipeId, foodId))

            findNavController().navigate(
                RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeEditorFragment()
            )
        }
    }
}