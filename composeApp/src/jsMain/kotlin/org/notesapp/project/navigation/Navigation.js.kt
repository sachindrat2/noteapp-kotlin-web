package org.notesapp.project.navigation

import kotlinx.browser.window

actual fun updateBrowserHistory(path: String) {
    try {
        window.history.pushState(null, "", path)
    } catch (e: Exception) {
        console.error("Failed to update browser history", e)
    }
}

actual fun getCurrentPath(): String {
    return window.location.pathname
}

actual fun setupBrowserNavigation(onNavigate: (String) -> Unit) {
    window.onpopstate = {
        onNavigate(window.location.pathname)
        null
    }
}
