package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entity.Income
import com.example.expensetracker.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    fun addIncome(income: Income){
        viewModelScope.launch((Dispatchers.IO)) {
            incomeRepository.addIncome(income)
        }
    }

    fun showIncome(): Flow<List<Income>> {
        return incomeRepository.showIncome()
    }
}