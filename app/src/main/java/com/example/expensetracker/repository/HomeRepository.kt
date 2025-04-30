package com.example.expensetracker.repository

import com.example.expensetracker.utils.DatastoreManager
import com.example.expensetracker.viewModel.HomeViewModel
import java.lang.reflect.Constructor
import javax.inject.Inject

class HomeRepository @Inject constructor(val datastoreManager: DatastoreManager){

    val savedUserName = datastoreManager.savedUserName
    suspend fun saveUserName(name:String){
        datastoreManager.saveUserName(name)
    }

}