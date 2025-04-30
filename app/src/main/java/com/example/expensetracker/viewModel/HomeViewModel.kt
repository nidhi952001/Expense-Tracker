package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.HomeRepository
import com.example.expensetracker.uiScreen.saveInitalMoneyScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        val homeRepository: HomeRepository
) : ViewModel(){

        val savedUsername = homeRepository.savedUserName
        fun saveUserNamee(name: String) {
                viewModelScope.launch {
                        homeRepository.saveUserName(name)
                }
        }


}