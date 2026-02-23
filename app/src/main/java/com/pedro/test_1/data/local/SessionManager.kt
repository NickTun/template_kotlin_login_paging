package com.pedro.test_1.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pedro.test_1.domain.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_USERNAME = stringPreferencesKey("username")
    }

    val isLoggedIn: Flow<Boolean>
        get() = context.dataStore.data.map { prefs -> prefs[KEY_LOGGED_IN] ?: false }

    val username: Flow<String?>
        get() = context.dataStore.data.map { prefs -> prefs[KEY_USERNAME] }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_USERNAME] = user.name
        }
    }

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs.remove(KEY_USERNAME)
        }
    }
}
