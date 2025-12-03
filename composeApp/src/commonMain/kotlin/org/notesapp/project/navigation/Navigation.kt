package org.notesapp.project.navigation

import androidx.compose.runtime.*

sealed class Route(val path: String) {
    object Login : Route("/login")
    object Register : Route("/register")
    object Notes : Route("/notes")
    object AddNote : Route("/notes/add")
    data class EditNote(val noteId: String) : Route("/notes/edit/$noteId")
    
    companion object {
        fun fromPath(path: String): Route {
            return when {
                path == "/login" -> Login
                path == "/register" -> Register
                path == "/notes" -> Notes
                path == "/notes/add" -> AddNote
                path.startsWith("/notes/edit/") -> {
                    val noteId = path.substringAfterLast("/")
                    EditNote(noteId)
                }
                else -> Notes // Default
            }
        }
    }
}

class NavigationController {
    private val _currentRoute = mutableStateOf<Route>(Route.Login)
    val currentRoute: State<Route> = _currentRoute
    
    private val _noteIdForEdit = mutableStateOf<String?>(null)
    val noteIdForEdit: State<String?> = _noteIdForEdit
    
    fun navigate(route: Route) {
        _currentRoute.value = route
        updateBrowserUrl(route)
        
        // Handle note editing
        when (route) {
            is Route.EditNote -> _noteIdForEdit.value = route.noteId
            else -> _noteIdForEdit.value = null
        }
    }
    
    fun goBack() {
        when (currentRoute.value) {
            is Route.Register -> navigate(Route.Login)
            is Route.AddNote, is Route.EditNote -> navigate(Route.Notes)
            else -> {} // Can't go back from login/notes
        }
    }
    
    private fun updateBrowserUrl(route: Route) {
        // Platform-specific implementation
        updateBrowserHistory(route.path)
    }
}

expect fun updateBrowserHistory(path: String)
expect fun getCurrentPath(): String
expect fun setupBrowserNavigation(onNavigate: (String) -> Unit)

val LocalNavigationController = compositionLocalOf<NavigationController> {
    error("NavigationController not provided")
}
