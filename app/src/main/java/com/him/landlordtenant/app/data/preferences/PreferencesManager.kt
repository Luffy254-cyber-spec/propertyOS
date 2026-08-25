package com.him.landlordtenant.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class PreferencesManager(private val context: Context) {

    private object PreferencesKeys {
        val USE_DARK_MODE = booleanPreferencesKey("use_dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val CURRENCY = stringPreferencesKey("currency")
        val BIOMETRIC_LOGIN = booleanPreferencesKey("biometric_login")
        val LOCATION_ENABLED = booleanPreferencesKey("location_enabled")
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                useDarkMode = preferences[PreferencesKeys.USE_DARK_MODE] ?: false,
                language = preferences[PreferencesKeys.LANGUAGE] ?: "en",
                currency = preferences[PreferencesKeys.CURRENCY] ?: "KES",
                biometricLogin = preferences[PreferencesKeys.BIOMETRIC_LOGIN] ?: false,
                locationEnabled = preferences[PreferencesKeys.LOCATION_ENABLED] ?: true,
                pushNotifications = preferences[PreferencesKeys.PUSH_NOTIFICATIONS] ?: true
            )
        }

    suspend fun updateDarkMode(useDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DARK_MODE] = useDarkMode
        }
    }

    suspend fun updateLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun updateCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENCY] = currency
        }
    }

    suspend fun updateBiometricLogin(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BIOMETRIC_LOGIN] = enabled
        }
    }

    suspend fun updateLocationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOCATION_ENABLED] = enabled
        }
    }

    suspend fun updatePushNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PUSH_NOTIFICATIONS] = enabled
        }
    }
}


