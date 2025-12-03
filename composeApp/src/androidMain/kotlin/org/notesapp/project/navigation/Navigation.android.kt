package org.notesapp.project.navigation

// Android doesn't have browser navigation
actual fun updateBrowserHistory(path: String) {
    // No-op for Android
}

actual fun getCurrentPath(): String {
    return "/notes" // Default to notes for Android
}

actual fun setupBrowserNavigation(onNavigate: (String) -> Unit) {
    // No-op for Android
}
