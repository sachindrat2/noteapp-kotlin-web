package org.notesapp.project.storage

actual class AuthStorage actual constructor() {
    actual fun saveToken(token: String) { TODO("Not yet implemented") }
    actual fun getToken(): String? = TODO("Not yet implemented")
    actual fun clearToken() { TODO("Not yet implemented") }
    actual fun saveUsername(username: String) { TODO("Not yet implemented") }
    actual fun getUsername(): String? = TODO("Not yet implemented")
    actual fun isLoggedIn(): Boolean = TODO("Not yet implemented")
    actual fun saveLanguage(languageCode: String) { TODO("Not yet implemented") }
    actual fun getLanguage(): String? = TODO("Not yet implemented")
}