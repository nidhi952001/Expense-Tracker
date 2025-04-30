package com.example.expensetracker.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "my_dataStore")
class DatastoreManager @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val userNameKey = stringPreferencesKey("username")

    val savedUserName : Flow<String> = context.dataStore.data.map {dataStore->
        dataStore[userNameKey]?:"User"
    }
    suspend fun saveUserName(name:String){
        context.dataStore.edit { dataStore->
            dataStore[userNameKey] = name
        }
    }
}