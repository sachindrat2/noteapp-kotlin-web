package org.notesapp.project.model

import androidx.compose.ui.graphics.Color
import org.notesapp.project.utils.currentTimeMillis

data class Note(
    val id: String = generateId(),
    val title: String = "",
    val content: String = "",
    val color: NoteColor = NoteColor.DEFAULT,
    val timestamp: Long = currentTimeMillis(),
    val isPinned: Boolean = false
) {
    companion object {
        private var counter = 0L
        fun generateId(): String = "note_${currentTimeMillis()}_${counter++}"
    }
}

enum class NoteColor(val color: Color, val lightColor: Color) {
    DEFAULT(Color(0xFFFFFFFF), Color(0xFFFAFAFA)),
    RED(Color(0xFFF28B82), Color(0xFFFCEAE8)),
    ORANGE(Color(0xFFFBBC04), Color(0xFFFEF7E0)),
    YELLOW(Color(0xFFFFF475), Color(0xFFFFFDE7)),
    GREEN(Color(0xFFCCFF90), Color(0xFFF1F8E9)),
    TEAL(Color(0xFFA7FFEB), Color(0xFFE0F7FA)),
    BLUE(Color(0xFFCBF0F8), Color(0xFFE1F5FE)),
    PURPLE(Color(0xFFAECBFA), Color(0xFFEDE7F6)),
    PINK(Color(0xFFFDCFE8), Color(0xFFFCE4EC)),
    BROWN(Color(0xFFE6C9A8), Color(0xFFEFEBE9)),
    GRAY(Color(0xFFE8EAED), Color(0xFFF5F5F5))
}
