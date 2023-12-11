package com.ownlab.ownlab_client.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ownlab.ownlab_client.module.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Manager (private val context: Context) {
    fun get(): Flow<String?> {
        return context.dataStore.data.map { pref ->
            pref[KEY]
        }
    }

    suspend fun save(token: String) {
        context.dataStore.edit { pref ->
            pref[KEY] = token
        }
    }

    suspend fun delete() {
        context.dataStore.edit { pref ->
            pref.remove(KEY)
        }
    }

    companion object {
        private val KEY = stringPreferencesKey("jwt_token")
    }
}