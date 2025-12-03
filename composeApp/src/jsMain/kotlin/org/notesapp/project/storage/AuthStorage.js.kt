package org.notesapp.project.storage

import kotlinx.browser.localStorage

actual class AuthStorage {
    actual fun saveToken(token: String) {
        localStorage.setItem(KEY_TOKEN, token)
    }
    
    actual fun getToken(): String? {
        return localStorage.getItem(KEY_TOKEN)
    }
    
    actual fun clearToken() {
        localStorage.removeItem(KEY_TOKEN)
        localStorage.removeItem(KEY_USERNAME)
    }
    
    actual fun saveUsername(username: String) {
        localStorage.setItem(KEY_USERNAME, username)
    }
    
    actual fun getUsername(): String? {
        return localStorage.getItem(KEY_USERNAME)
    }
    
    actual fun isLoggedIn(): Boolean {
        return getToken() != null
    }
    
    actual fun saveLanguage(languageCode: String) {
        localStorage.setItem(KEY_LANGUAGE, languageCode)
    }
    
    actual fun getLanguage(): String? {
        return localStorage.getItem(KEY_LANGUAGE)
    }
    
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_LANGUAGE = "app_language"
    }
}
