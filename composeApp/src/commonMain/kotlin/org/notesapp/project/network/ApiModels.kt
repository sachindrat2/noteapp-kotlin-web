package org.notesapp.project.network

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val username: String,  // API uses username for email
    val password: String
)

@Serializable
data class TokenResponse(
    val access_token: String,
    val token_type: String = "bearer",
    val expires_in: Int? = null
)

@Serializable
data class ApiNote(
    val id: Int? = null,  // API returns integer ID
    val title: String,
    val content: String,
    val color: String? = null,  // Optional - API might not return this
    @kotlinx.serialization.SerialName("is_favorite")
    val isPinned: Boolean = false,
    val timestamp: Long? = null,
    @kotlinx.serialization.SerialName("created_at")
    val createdAt: String? = null,  // API returns string timestamp
    @kotlinx.serialization.SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class NotesResponse(
    val notes: List<ApiNote>
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String? = null
)
