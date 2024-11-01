package app.kotlin.currencyconverter.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A repository for managing user preferences using [DataStore].
 *
 * This repository provides methods for saving and retrieving user preferences, such as
 * the theme preference (dark or light mode) and whether the tips screen has been shown.
 *
 * @param dataStore The [DataStore] instance used for storing and retrieving user preferences.
 */
class UserPreferenceRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        /** Key for storing the user's theme preference (dark or light mode). */
        val IS_DARK_THEME = booleanPreferencesKey(name = "is_dark_theme")

        /** Key for storing whether the user has seen the tips screen. */
        val TIPS_SCREEN_SHOWN = booleanPreferencesKey(name = "tips_screen_shown")
    }

    /**
     * Saves the user's theme preference (dark or light mode) to [DataStore].
     *
     * @param isDarkTheme A Boolean indicating whether dark theme is enabled.
     */
    suspend fun saveThemePreference(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    /**
     * Saves the user's preference for whether the tips screen has been shown.
     *
     * @param tipsScreenShown A Boolean indicating whether the tips screen has been displayed to the user.
     */
    suspend fun saveTipsScreenShowingPreference(tipsScreenShown: Boolean) {
        dataStore.edit { preferences ->
            preferences[TIPS_SCREEN_SHOWN] = tipsScreenShown
        }
    }

    /**
     * A [Flow] that emits the user's preference for whether the tips screen has been shown.
     * The default value is `true` if no preference has been set.
     */
    val tipsScreenShown: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TIPS_SCREEN_SHOWN] ?: true
    }

    /**
     * A [Flow] that emits the user's theme preference. If no preference has been set,
     * the default value is `false`, indicating light mode.
     */
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: false
    }
}
