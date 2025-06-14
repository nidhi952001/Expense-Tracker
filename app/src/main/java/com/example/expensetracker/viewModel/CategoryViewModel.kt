package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Category
import com.example.expensetracker.entity.CategoryType
import com.example.expensetracker.repository.CategoryRepository
import com.example.expensetracker.uiScreen.uiState.CategoryInputState
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


    val listOfExpCategory:StateFlow<List<Category>> = categoryRepository.showCategoryByType(CategoryType.EXPENSE).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    val listOfIncCategory = categoryRepository.showCategoryByType(CategoryType.INCOME).stateIn(
        viewModelScope,SharingStarted.WhileSubscribed(), emptyList()
    )

    fun addCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.addCategory(category)
        }
    }

    //temp ui state for category screen
    private val _temCatUiState = MutableStateFlow(CategoryInputState())
    val tempCategoryState = _temCatUiState.asStateFlow()
    fun updateSelectedCategory(selectedCategory: Int) {
        _temCatUiState.update {
            it.copy(
                selectedExpCategoryId = selectedCategory
            )
        }
        resetIncCategory()
        if(selectedCategory!=0) _temCatUiState.update { it.copy(isExpenseCategoryValid = true) }
        else _temCatUiState.update { it.copy(isExpenseCategoryValid = false) }

        //this will update the category , which going to be use to save into database
        categoryRepository.updateselectedCategory(selectedCategory)
    }
    fun updateSelectedIncCategory(selectedCategory: Int){
        _temCatUiState.update {
            it.copy(
                selectedIncCategoryId = selectedCategory
            )
        }
        resetExpCategory()
        if(selectedCategory!=0) _temCatUiState.update { it.copy(isIncomeCategoryValid = true) }
        else _temCatUiState.update { it.copy(isIncomeCategoryValid = false) }
        //this will update the category , which going to be use to save into database
        categoryRepository.updateselectedCategory(selectedCategory)
    }

    //this both show on expense screen
    val currentExpCategory = _temCatUiState.map {
        it.selectedExpCategoryId
    }.distinctUntilChanged().flatMapLatest {
        categoryRepository.getCategoryNameById(it)
    }

    val currentIncCategory = _temCatUiState.map {
        it.selectedIncCategoryId
    }.distinctUntilChanged().flatMapLatest {
        categoryRepository.getCategoryNameById(it)
    }

    fun resetIncCategory(){
        _temCatUiState.update {
            it.copy(
                selectedIncCategoryId = 0,
                isIncomeCategoryValid = false,
            )
        }
    }

    fun resetExpCategory(){
        _temCatUiState.update {
            it.copy(
                selectedExpCategoryId = 0,
                isExpenseCategoryValid = false
            )
        }
    }



}