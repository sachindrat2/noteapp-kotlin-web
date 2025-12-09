package org.notesapp.project
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.notesapp.project.i18n.ProvideLocalization
import org.notesapp.project.navigation.*
import org.notesapp.project.network.NotesApiService
import org.notesapp.project.storage.AuthStorage
import org.notesapp.project.ui.screens.AddEditNoteScreen
import org.notesapp.project.ui.screens.LoginScreen
import org.notesapp.project.ui.screens.NotesListScreen
import org.notesapp.project.ui.screens.RegisterScreen
import org.notesapp.project.ui.theme.NotesAppTheme

import androidx.compose.material3.Text
import org.notesapp.project.viewmodel.NotesViewModel

@Composable
fun App() {
    val authStorage = remember { AuthStorage() }
    val navigationController = remember { NavigationController() }
    val apiService = remember { NotesApiService.getInstance() }
    // Setup browser navigation for web
    LaunchedEffect(Unit) {
        setupBrowserNavigation { path ->
            val route = Route.fromPath(path)
            navigationController.navigate(route)
        }
        // Initialize route from current URL
        val currentPath = getCurrentPath()
        val initialRoute = if (apiService.isLoggedIn()) {
            Route.fromPath(currentPath)
        } else {
            Route.Login
        }
        navigationController.navigate(initialRoute)
    }
    NotesAppTheme {
        ProvideLocalization(authStorage = authStorage) {
            CompositionLocalProvider(LocalNavigationController provides navigationController) {
                // Minimal Japanese text for debugging
                Text("こんにちは世界")
                AppContent(
                    navigationController = navigationController,
                    apiService = apiService
                )
            }
        }
    }
}

@Composable
private fun AppContent(
    navigationController: NavigationController,
    apiService: NotesApiService
) {
    val viewModel: NotesViewModel = viewModel { NotesViewModel() }
    val currentRoute by navigationController.currentRoute
    val noteIdForEdit by navigationController.noteIdForEdit

    // Find note for editing
    val editingNote = noteIdForEdit?.let { id ->
        viewModel.notes.find { it.id == id }
    }

    // Load notes if already logged in
    LaunchedEffect(Unit) {
        if (apiService.isLoggedIn()) {
            viewModel.loadNotesFromApi()
        }
    }
    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            when (targetState) {
                is Route.Login, is Route.Register -> {
                    fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
                }
                is Route.AddNote, is Route.EditNote -> {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn() togetherWith
                    slideOutVertically(
                        targetOffsetY = { -it / 3 },
                        animationSpec = tween(300)
                    ) + fadeOut()
                }
                is Route.Notes -> {
                    slideInVertically(
                        initialOffsetY = { -it / 3 },
                        animationSpec = tween(300)
                    ) + fadeIn() togetherWith
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeOut()
                }
            }
        }
    ) { route ->
        when (route) {
            is Route.Login -> {
                LoginScreen(
                    onLoginSuccess = {
                        viewModel.loadNotesFromApi()
                        navigationController.navigate(Route.Notes)
                    },
                    onNavigateToRegister = {
                        navigationController.navigate(Route.Register)
                    }
                )
            }
            is Route.Register -> {
                RegisterScreen(
                    onRegisterSuccess = {
                        viewModel.loadNotesFromApi()
                        navigationController.navigate(Route.Notes)
                    },
                    onBackToLogin = {
                        navigationController.navigate(Route.Login)
                    }
                )
            }
            is Route.Notes -> {
                NotesListScreen(
                    viewModel = viewModel,
                    onNoteClick = { note ->
                        navigationController.navigate(Route.EditNote(note.id))
                    },
                    onAddNoteClick = {
                        navigationController.navigate(Route.AddNote)
                    },
                    onLogout = {
                        apiService.logout()
                        viewModel.clearNotes()
                        navigationController.navigate(Route.Login)
                    }
                )
            }
            is Route.AddNote, is Route.EditNote -> {
                AddEditNoteScreen(
                    note = editingNote,
                    onSave = { note ->
                        if (editingNote != null) {
                            viewModel.updateNote(note)
                        } else {
                            viewModel.addNote(note)
                        }
                        navigationController.navigate(Route.Notes)
                    },
                    onBack = {
                        navigationController.goBack()
                    }
                )
            }
        }
    }
}