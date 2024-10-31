package app.kotlin.currencyconverter.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val IS_DARK_THEME = booleanPreferencesKey(name = "is_dark_theme")
        val TIPS_SCREEN_SHOWN = booleanPreferencesKey(name = "tips_screen_shown")
    }

    suspend fun saveThemePreference(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    suspend fun saveTipsScreenShowingPreference(tipsScreenShown: Boolean) {
        dataStore.edit { preferences ->
            preferences[TIPS_SCREEN_SHOWN] = tipsScreenShown
        }
    }

    val tipsScreenShown: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TIPS_SCREEN_SHOWN] ?: true
    }
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: false
    }
}