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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "my_dataStore")
class DatastoreManager @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val userNameKey = stringPreferencesKey("username")
    private val initialAmountKey = stringPreferencesKey("not_decided")

    val savedUserName : Flow<String> = context.dataStore.data.map {dataStore->
        dataStore[userNameKey]?:"User"
    }

    val getInitialAmount: Flow<String> = context.dataStore.data.map {
        it[initialAmountKey]?:"not_decided"
    }
    suspend fun saveUserName(name:String){
        context.dataStore.edit { dataStore->
            dataStore[userNameKey] = name
        }
    }

    suspend fun saveInitialAmount(amount:String){
        context.dataStore.edit {
            it[initialAmountKey] = amount
        }
    }
}