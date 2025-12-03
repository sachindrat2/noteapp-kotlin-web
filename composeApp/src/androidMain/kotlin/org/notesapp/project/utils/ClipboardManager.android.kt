package org.notesapp.project.utils

import android.content.ClipData
import android.content.Context

actual class ClipboardManager {
    actual fun copyToClipboard(text: String) {
        // TODO: Implement with proper Context injection
        // For now, this is a placeholder
        println("Copy to clipboard: $text")
    }
    
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("note", text)
        clipboard.setPrimaryClip(clip)
    }
}
