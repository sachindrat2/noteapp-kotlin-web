package org.notesapp.project.utils

expect fun currentTimeMillis(): Long

expect fun formatRelativeTime(timestamp: Long): String

expect fun formatFullDateTime(timestamp: Long): String

expect fun formatDate(timestamp: Long): String

expect fun formatTime(timestamp: Long): String
