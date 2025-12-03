package org.notesapp.project.storage

// Expect declaration for platform-specific storage
expect class AuthStorage() {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
    fun saveUsername(username: String)
    fun getUsername(): String?
    fun isLoggedIn(): Boolean
    fun saveLanguage(languageCode: String)
    fun getLanguage(): String?
}
