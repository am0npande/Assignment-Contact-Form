package com.example.contactform.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreManager@Inject constructor(@ApplicationContext private val context: Context) {

    // Define the DataStore name and create it as an extension on Context
    private val Context.dataStore by preferencesDataStore("user_prefs")

    // Define a key for storing the ID
    private val ID_KEY = intPreferencesKey("user_id")

    // Function to save the ID number
    suspend fun saveId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }

    val id: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[ID_KEY]
        }
}