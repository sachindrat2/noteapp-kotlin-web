package org.notesapp.project.utils

import kotlinx.browser.window

actual class ClipboardManager {
    actual fun copyToClipboard(text: String) {
        try {
            // Use modern Clipboard API
            window.navigator.clipboard.writeText(text)
        } catch (e: Exception) {
            // Fallback for older browsers
            console.log("Clipboard API not available, using fallback")
            fallbackCopyToClipboard(text)
        }
    }
    
    private fun fallbackCopyToClipboard(text: String) {
        val textArea = kotlinx.browser.document.createElement("textarea") as org.w3c.dom.HTMLTextAreaElement
        textArea.value = text
        textArea.style.position = "fixed"
        textArea.style.left = "-9999px"
        kotlinx.browser.document.body?.appendChild(textArea)
        textArea.select()
        try {
            kotlinx.browser.document.execCommand("copy")
        } catch (e: Exception) {
            console.error("Failed to copy text", e)
        }
        kotlinx.browser.document.body?.removeChild(textArea)
    }
}
