package org.notesapp.project.utils

object Constants {
    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 200
    const val ANIMATION_DURATION_MEDIUM = 300
    const val ANIMATION_DURATION_LONG = 400
    
    // Grid Configuration
    const val GRID_MIN_ITEM_WIDTH = 150
    const val GRID_SPACING = 12
    
    // Note Configuration
    const val MAX_NOTE_TITLE_LENGTH = 100
    const val MAX_NOTE_CONTENT_LENGTH = 10000
    
    // Search Configuration
    const val SEARCH_DEBOUNCE_MILLIS = 300L
    
    // UI Configuration
    const val CARD_CORNER_RADIUS = 16
    const val FAB_SIZE = 56
    const val ICON_BUTTON_SIZE = 48
    
    // API Configuration (for future use)
    object Api {
        const val BASE_URL = "https://api.notesapp.com"
        const val TIMEOUT_SECONDS = 30L
        const val MAX_RETRY_ATTEMPTS = 3
    }
    
    // Storage Keys
    object Storage {
        const val NOTES_KEY = "notes_data"
        const val USER_PREFERENCES = "user_preferences"
        const val THEME_MODE = "theme_mode"
        const val VIEW_MODE = "view_mode"
    }
}
