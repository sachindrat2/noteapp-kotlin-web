package org.notesapp.project.utils

import kotlin.js.Date

actual fun currentTimeMillis(): Long = Date.now().toLong()

actual fun formatRelativeTime(timestamp: Long): String {
    val now = currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> {
            val date = Date(timestamp.toDouble())
            "${date.toLocaleDateString()}"
        }
    }
}

actual fun formatFullDateTime(timestamp: Long): String {
    val date = Date(timestamp.toDouble())
    return "${date.toLocaleDateString()} at ${date.toLocaleTimeString()}"
}

actual fun formatDate(timestamp: Long): String {
    val date = Date(timestamp.toDouble())
    return date.toLocaleDateString()
}

actual fun formatTime(timestamp: Long): String {
    val date = Date(timestamp.toDouble())
    return date.toLocaleTimeString()
}
