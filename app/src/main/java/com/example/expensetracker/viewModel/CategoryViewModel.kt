package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Category
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.utils.InputUIState.CategoryInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
):ViewModel(){

    val listOfCategory:StateFlow<List<Category>> = categoryRepository.showCategory().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    fun addCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.addCategory(category)
        }
    }

    //temp ui state
    private val _temCatUiState = MutableStateFlow(CategoryInputState())
    val tempCategoryState = _temCatUiState.asStateFlow()
    fun updateSelectedCategory(selectedCategory: Int) {
        _temCatUiState.update {
            it.copy(
                selectedCategoryId = selectedCategory
            )
        }
        if(selectedCategory!=0) _temCatUiState.update { it.copy(validExpCategory = true) }
        else _temCatUiState.update { it.copy(validExpCategory = false) }
        categoryRepository.updateselectedCategory(selectedCategory)
    }

    val currentExpCategory = _temCatUiState.map {
        it.selectedCategoryId
    }.distinctUntilChanged().flatMapLatest {
        categoryRepository.getCategoryById(it)
    }


}