package org.notesapp.project.utils

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual class ClipboardManager {
    actual fun copyToClipboard(text: String) {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val stringSelection = StringSelection(text)
            clipboard.setContents(stringSelection, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
