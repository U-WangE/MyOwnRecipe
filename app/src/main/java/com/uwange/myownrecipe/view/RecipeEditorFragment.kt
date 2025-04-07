package com.uwange.myownrecipe.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uwange.myownrecipe.R
import com.uwange.myownrecipe.Util.formatScoreAsString
import com.uwange.myownrecipe.Util.setGlideUrlToImage
import com.uwange.myownrecipe.data.RecipeArgumentData
import com.uwange.myownrecipe.data.ResponseForm
import com.uwange.myownrecipe.databinding.FragmentRecipeEditorBinding
import com.uwange.myownrecipe.util.EditableSpinner
import com.uwange.myownrecipe.viewModel.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeEditorFragment : Fragment() {
    private var _binding: FragmentRecipeEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipeEditorViewModel

    private var editableSpinner: EditableSpinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RecipeEditorViewModel::class.java]
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ResponseForm.Loading -> {

                            uiSetting()
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
        clickListener()

        viewModel.getRecipe().let {
            setGlideUrlToImage(binding.ivFoodImage, it?.imageUrl?:"")
            binding.ivFoodImage.contentDescription = it?.imageDescription?:""

            setBookmarkView(it?.bookmark?:false)

            setupFoodCategorySpinner(it?.foodId)

            binding.tvFoodName.text = viewModel.getFoodName()
            //TODO:: SCORE 입력 양식 정규식 적용 필요
            binding.etScore.setText(
                formatScoreAsString(it?.score?:"")
            )
            binding.etRecipeTitle.setText(it?.name?:"")
            binding.etRecipeSteps.setText(it?.recipeSteps?:"")
            binding.etIngredients.setText(it?.ingredients?:"")
            binding.etRecipeReview.setText(it?.recipeReview?:"")
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
        tag = isBookmarked
    }

    private fun clickListener() {
        binding.tvSaveBtn.setOnClickListener {
            //TODO Save 관련 기능 추가
        }

        //TODO Image Setting 처리

        //TODO Back Button 처리


        binding.ivBookmark.setOnClickListener {
            val isBookmarked = binding.ivBookmark.tag as? Boolean ?: false
            setBookmarkView(!isBookmarked)
        }
    }

    private fun setupFoodCategorySpinner(foodId: Int? = null) {
        //TODO Spinner 적용
        editableSpinner = EditableSpinner(binding.spFoodCategory, binding.etFoodCategory).apply {
            setup(viewModel.getFoodCategoryList())
            setInitValue(foodId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO:: 종료사 recipeEditorArgumentData null 로 초기화 해야함
    }
}