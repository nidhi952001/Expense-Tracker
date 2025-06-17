package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.HomeRepository
import com.example.expensetracker.uiScreen.uiState.HomeStateData
import com.example.expensetracker.uiScreen.uiState.SelectedTopBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        val homeRepository: HomeRepository
) : ViewModel(){

        //data = store
        val savedUsername = homeRepository.savedUserName
        //selected top bar for income and expesnse
        private val _selectedFinanceType = MutableStateFlow(SelectedTopBar())
        val selectedFinance = _selectedFinanceType.asStateFlow()

        //ui state for home
        private val _homeUiState = MutableStateFlow(HomeStateData())
        val homeUiStateData = _homeUiState.asStateFlow()
        fun saveUserName(name: String) {
                viewModelScope.launch {
                        _homeUiState.update {
                                it.copy(userName = name)
                        }
                        homeRepository.saveUserName(name)
                }
        }

        fun updateUserName(name: String){
                _homeUiState.update { current->
                        current.copy(
                                userName = name
                        )
                }
        }

        fun updateInitalMoney(amount:String){
                _homeUiState.update {
                        it.copy(
                                initialMoney = amount
                        )
                }
        }

        fun updateSelectedFinance(selected:Int) {
                _selectedFinanceType.update {
                        it.copy(selectedFinance = selected)
                }
        }


}