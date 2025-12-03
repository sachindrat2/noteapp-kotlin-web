package org.notesapp.project.i18n

import androidx.compose.runtime.*
import org.notesapp.project.storage.AuthStorage

class LocalizationManager(private val authStorage: AuthStorage) {
    private val _currentLanguage = mutableStateOf(loadSavedLanguage())
    val currentLanguage: State<Language> = _currentLanguage
    
    private fun loadSavedLanguage(): Language {
        val savedCode = authStorage.getLanguage()
        return if (savedCode != null) {
            Language.fromCode(savedCode)
        } else {
            Language.ENGLISH
        }
    }
    
    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        authStorage.saveLanguage(language.code)
    }
    
    fun getString(key: StringKey): String {
        return when (currentLanguage.value) {
            Language.ENGLISH -> key.en
            Language.JAPANESE -> key.ja
        }
    }
}

val LocalLocalizationManager = compositionLocalOf<LocalizationManager> {
    error("LocalizationManager not provided")
}

@Composable
fun ProvideLocalization(
    authStorage: AuthStorage,
    content: @Composable () -> Unit
) {
    val localizationManager = remember { LocalizationManager(authStorage) }
    CompositionLocalProvider(LocalLocalizationManager provides localizationManager) {
        content()
    }
}

@Composable
fun rememberStrings(): Strings {
    val localizationManager = LocalLocalizationManager.current
    val language by localizationManager.currentLanguage
    return remember(language) { Strings }
}
