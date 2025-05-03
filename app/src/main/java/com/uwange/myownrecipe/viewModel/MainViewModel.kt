package com.uwange.myownrecipe.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    fun saveData(key: String, value: Any) {
        savedStateHandle[key] = value
    }

    fun getData(key: String): Any? {
        return savedStateHandle[key]
    }

    fun <T> getDataStateFlow(key: String, defaultValue: T): StateFlow<T> {
        return savedStateHandle.getStateFlow(key, defaultValue)
    }
}