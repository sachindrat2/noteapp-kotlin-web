package org.notesapp.project.storage

import android.content.Context
import android.content.SharedPreferences

actual class AuthStorage {
    private val prefs: SharedPreferences by lazy {
        // We'll need to pass context, for now use a placeholder
        // In production, use dependency injection or Application context
        throw NotImplementedError("Android implementation requires Context")
    }
    
    actual fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }
    
    actual fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    actual fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USERNAME).apply()
    }
    
    actual fun saveUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }
    
    actual fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }
    
    actual fun isLoggedIn(): Boolean {
        return getToken() != null
    }
    
    actual fun saveLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }
    
    actual fun getLanguage(): String? {
        return prefs.getString(KEY_LANGUAGE, null)
    }
    
    companion object {
        private const val PREFS_NAME = "notes_app_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_LANGUAGE = "app_language"
    }
}
