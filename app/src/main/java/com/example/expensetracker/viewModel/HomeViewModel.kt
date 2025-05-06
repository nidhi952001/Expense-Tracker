package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.HomeRepository
import com.example.expensetracker.utils.InputUIState.HomeStateData
import com.example.expensetracker.utils.InputUIState.TopBarStateData
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

        //ui state for home
        private val _homeUiState = MutableStateFlow(HomeStateData())
        val homeUiStateData = _homeUiState.asStateFlow()

        //ui state for top bar
        private val _topBarUiState = MutableStateFlow(TopBarStateData())
        val topBarUiState = _topBarUiState.asStateFlow()

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

        fun changeToNewRoute(newSelection:String){
                _topBarUiState.update {
                        it.copy(selectedTopBar = newSelection)
                }
        }


}