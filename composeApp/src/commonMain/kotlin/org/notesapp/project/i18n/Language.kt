package org.notesapp.project.i18n

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    JAPANESE("ja", "日本語");
    
    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
