package com.uwange.myownrecipe.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.util.Util.formatScoreAsString
import com.uwange.myownrecipe.util.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.FoodArgumentData
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.databinding.FragmentRecipeEditorBinding
import com.uwange.myownrecipe.viewModel.MainViewModel
import com.uwange.myownrecipe.viewModel.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeEditorFragment : Fragment() {
    private var _binding: FragmentRecipeEditorBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)
    private val viewModel: RecipeEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeEditorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSavedStateData(mainViewModel.getData("foodArgumentData") as FoodArgumentData)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect {
                        // TODO:: 로딩 처리
                    }
                }
                launch {
                    viewModel.isError.collect {
                        // TODO:: Error 처리
                    }
                }
                launch {
                    viewModel.recipeItem.collect { recipeItem ->
                        uiSetting(recipeItem)
                        clickListener()
                    }
                }
                launch {
                    viewModel.saveState.collectLatest {
                        if (it)
                            findNavController().navigate(
                                RecipeEditorFragmentDirections.actionRecipeEditorFragmentToRecipeDetailFragment()
                            )
                    }
                }
            }
        }
    }

    private fun uiSetting(recipeItem: RecipeItem) {
        recipeItem.let {
            setGlideUrlToImage(binding.ivFoodImage, it.imageUrl)
            binding.ivFoodImage.contentDescription = it.imageDescription

            setBookmarkView(it.bookmark)

            binding.tvFoodName.text = viewModel.getFoodName()

            //TODO:: SCORE 입력 양식 정규식 적용 필요
            binding.etScore.setText(
                formatScoreAsString(it.score)
            )
            binding.etRecipeTitle.setText(it.recipeName)
            binding.etRecipeSteps.setText(it.recipeSteps)
            binding.etIngredients.setText(it.ingredients)
            binding.etRecipeReview.setText(it.recipeReview)
        }
    }

    private fun setBookmarkView(isBookmarked: Boolean) = with(binding.ivBookmark) {
        setBackgroundResource(
            if (isBookmarked) R.drawable.ic_bookmark_24 else R.drawable.ic_bookmark_border_24
        )
        backgroundTintList = resources.getColorStateList(
            if (isBookmarked) R.color.yellow_600 else R.color.gray_300,
            null
        )
        tag = isBookmarked
    }

    private fun clickListener() {
        binding.tvSaveBtn.setOnClickListener {
            //TODO:: 해당 값 저장시 bookmark 변경되면, food Item 에 해당 값 저장해야함
            viewModel.saveRecipeItem(
                binding.etRecipeTitle.text.toString(),
                binding.ivBookmark.tag as? Boolean ?: false,
                binding.etScore.text.toString(),
                binding.etIngredients.text.toString(),
                binding.etRecipeSteps.text.toString(),
                binding.etRecipeReview.text.toString()
            )
        }

        //TODO Image Setting 처리

        //TODO Back Button 처리


        binding.ivBookmark.setOnClickListener {
            val isBookmarked = binding.ivBookmark.tag as? Boolean ?: false
            setBookmarkView(!isBookmarked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO:: 종료사 recipeEditorArgumentData null 로 초기화 해야함
    }
}