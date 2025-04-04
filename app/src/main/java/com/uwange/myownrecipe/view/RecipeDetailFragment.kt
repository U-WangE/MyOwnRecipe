package com.uwange.myownrecipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentRecipeDetailBinding
import com.uwange.myownrecipe.viewModel.RecipeDetailViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
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
        // Set Recipe Image
//        setGlideUrlToImage(binding.ivFoodImage, viewModel.getFoodImage())
        // Set Bookmark State
//        setBookmarkView(viewModel.getBookmarkState() == true)

//        binding.tvFoodName.text = viewModel.getFoodName()
//        binding.tvScore.text = formatScoreAsString(viewModel.getScore())
//        binding.tvRecipeTitle.text = viewModel.getRecipeName()
//        binding.tvIngredients.text = viewModel.getIngredients()
//        binding.tvRecipeSteps.text = viewModel.getRecipeSteps()
//        binding.tvRecipeReview.text = viewModel.getRecipeReview()
    }

    private fun setBookmarkView(isBookmarked: Boolean) = with(binding.ivBookmark) {
        setBackgroundResource(
            if (isBookmarked) R.drawable.ic_bookmark_24 else R.drawable.ic_bookmark_border_24
        )
        backgroundTintList = resources.getColorStateList(
            if (isBookmarked) R.color.yellow_600 else R.color.gray_300,
            null
        )
    }
}