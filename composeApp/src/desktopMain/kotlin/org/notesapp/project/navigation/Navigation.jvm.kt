package org.notesapp.project.navigation

// Desktop doesn't have browser navigation
actual fun updateBrowserHistory(path: String) {
    // No-op for desktop
}

actual fun getCurrentPath(): String {
    return "/notes" // Default to notes for desktop
}

actual fun setupBrowserNavigation(onNavigate: (String) -> Unit) {
    // No-op for desktop
}
