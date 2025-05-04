package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.HomeRepository
import com.example.expensetracker.utils.UiStateData
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

        //ui state
        private val _uiState = MutableStateFlow<UiStateData>(UiStateData())
        val uiStateData = _uiState.asStateFlow()

        val savedUsername = homeRepository.savedUserName

        fun saveUserName(name: String) {
                viewModelScope.launch {
                        uiStateData.value.userName = name
                        homeRepository.saveUserName(name)
                }
        }

        fun updateUserName(name: String){
                _uiState.update { current->
                        current.copy(
                                userName = name
                        )
                }
        }

        fun updateInitalMoney(amount:String){
                _uiState.update {
                        it.copy(
                                initialMoney = amount
                        )
                }
        }

        fun changeToNewRoute(newSelection:String){
                _uiState.update {
                        it.copy(selected = newSelection)
                }
        }


}