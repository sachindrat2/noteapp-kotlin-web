package org.notesapp.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp

fun main() = application {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Notes App",
        state = windowState
    ) {
        App()
    }
}
