package com.example.expensetracker.repository

import android.util.Log
import com.example.expensetracker.utils.DatastoreManager
import com.example.expensetracker.viewModel.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import java.lang.reflect.Constructor
import javax.inject.Inject

class HomeRepository @Inject constructor(private val datastoreManager: DatastoreManager){

    val savedUserName = datastoreManager.savedUserName
        .catch {
            emit("")
        }
    suspend fun saveUserName(name: String) {
        require(name.isNotBlank()) { "Name cannot be blank" }
        datastoreManager.saveUserName(name.trim())
    }

}