package org.notesapp.project.storage

import java.util.prefs.Preferences

actual class AuthStorage {
    private val prefs: Preferences = Preferences.userNodeForPackage(AuthStorage::class.java)
    
    actual fun saveToken(token: String) {
        prefs.put(KEY_TOKEN, token)
        prefs.flush()
    }
    
    actual fun getToken(): String? {
        return prefs.get(KEY_TOKEN, null)
    }
    
    actual fun clearToken() {
        prefs.remove(KEY_TOKEN)
        prefs.remove(KEY_USERNAME)
        prefs.flush()
    }
    
    actual fun saveUsername(username: String) {
        prefs.put(KEY_USERNAME, username)
        prefs.flush()
    }
    
    actual fun getUsername(): String? {
        return prefs.get(KEY_USERNAME, null)
    }
    
    actual fun isLoggedIn(): Boolean {
        return getToken() != null
    }
    
    actual fun saveLanguage(languageCode: String) {
        prefs.put(KEY_LANGUAGE, languageCode)
        prefs.flush()
    }
    
    actual fun getLanguage(): String? {
        return prefs.get(KEY_LANGUAGE, null)
    }
    
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_LANGUAGE = "app_language"
    }
}
