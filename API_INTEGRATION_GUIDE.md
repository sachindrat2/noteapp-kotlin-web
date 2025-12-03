# 🔌 API Integration Guide

## Overview
This document outlines the plan for integrating REST API functionality into the Notes App.

## 📋 Phase 1: Preparation (Current)
- ✅ Pixel-perfect UI design
- ✅ Smooth animations
- ✅ Local state management
- ✅ MVVM architecture

## 🚀 Phase 2: API Integration (Next Steps)

### 1. Dependencies to Add

Add to `gradle/libs.versions.toml`:
```toml
[versions]
ktor = "2.3.7"
kotlinx-serialization = "1.6.2"
kotlinx-coroutines = "1.7.3"
kotlinx-datetime = "0.5.0"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-js = { module = "io.ktor:ktor-client-js", version.ref = "ktor" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }

[plugins]
kotlinx-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

### 2. Data Models

Update `Note.kt` with serialization:
```kotlin
@Serializable
data class Note(
    val id: String = generateId(),
    val title: String = "",
    val content: String = "",
    val color: NoteColor = NoteColor.DEFAULT,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val userId: String? = null,
    val syncedAt: Long? = null,
    val isDeleted: Boolean = false
)

@Serializable
data class CreateNoteRequest(
    val title: String,
    val content: String,
    val color: String,
    val isPinned: Boolean = false
)

@Serializable
data class UpdateNoteRequest(
    val title: String,
    val content: String,
    val color: String,
    val isPinned: Boolean
)

@Serializable
data class NotesResponse(
    val notes: List<Note>,
    val total: Int
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)
```

### 3. API Service

Create `api/NotesApiService.kt`:
```kotlin
interface NotesApiService {
    suspend fun getNotes(
        page: Int = 1,
        limit: Int = 50,
        search: String? = null
    ): Result<NotesResponse>
    
    suspend fun createNote(request: CreateNoteRequest): Result<Note>
    suspend fun updateNote(id: String, request: UpdateNoteRequest): Result<Note>
    suspend fun deleteNote(id: String): Result<Boolean>
    suspend fun togglePin(id: String): Result<Note>
    suspend fun syncNotes(lastSync: Long): Result<NotesResponse>
}

class NotesApiServiceImpl(
    private val client: HttpClient
) : NotesApiService {
    private val baseUrl = Constants.Api.BASE_URL
    
    override suspend fun getNotes(
        page: Int,
        limit: Int,
        search: String?
    ): Result<NotesResponse> = try {
        val response = client.get("$baseUrl/notes") {
            parameter("page", page)
            parameter("limit", limit)
            search?.let { parameter("search", it) }
        }.body<ApiResponse<NotesResponse>>()
        
        if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Unknown error"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override suspend fun createNote(request: CreateNoteRequest): Result<Note> = try {
        val response = client.post("$baseUrl/notes") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ApiResponse<Note>>()
        
        if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Failed to create note"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Implement other methods similarly...
}
```

### 4. HTTP Client Setup

Create `api/HttpClientFactory.kt`:
```kotlin
expect fun createHttpClient(): HttpClient

// Common configuration
fun httpClient(engine: HttpClient): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            // Add auth token header
            header(HttpHeaders.Authorization, "Bearer ${getAuthToken()}")
        }
    }
}
```

Platform-specific implementations:
```kotlin
// androidMain
actual fun createHttpClient(): HttpClient = httpClient(Android)

// iosMain
actual fun createHttpClient(): HttpClient = httpClient(Darwin)

// jsMain
actual fun createHttpClient(): HttpClient = httpClient(Js)
```

### 5. Repository Pattern

Create `repository/NotesRepository.kt`:
```kotlin
class NotesRepository(
    private val apiService: NotesApiService,
    private val localDataSource: LocalNotesDataSource // For offline support
) {
    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())
    val notesFlow: StateFlow<List<Note>> = _notesFlow.asStateFlow()
    
    suspend fun syncNotes() {
        val result = apiService.getNotes()
        result.onSuccess { response ->
            _notesFlow.value = response.notes
            localDataSource.saveNotes(response.notes)
        }.onFailure { error ->
            // Load from local cache on failure
            _notesFlow.value = localDataSource.getNotes()
        }
    }
    
    suspend fun createNote(note: Note): Result<Note> {
        val request = CreateNoteRequest(
            title = note.title,
            content = note.content,
            color = note.color.name,
            isPinned = note.isPinned
        )
        
        return apiService.createNote(request).also { result ->
            result.onSuccess { createdNote ->
                _notesFlow.value = _notesFlow.value + createdNote
                localDataSource.saveNote(createdNote)
            }
        }
    }
    
    // Implement other CRUD operations...
}
```

### 6. ViewModel Updates

Update `NotesViewModel.kt`:
```kotlin
class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.notesFlow.collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
        syncNotes()
    }
    
    fun syncNotes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.syncNotes()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    
    fun addNote(note: Note) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.createNote(note).fold(
                onSuccess = { /* Handle success */ },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isGridView: Boolean = true
)
```

### 7. Authentication

Create `auth/AuthService.kt`:
```kotlin
interface AuthService {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun logout(): Result<Boolean>
    suspend fun refreshToken(): Result<String>
    fun getAuthToken(): String?
}

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Long
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val user: User,
    val token: String,
    val refreshToken: String
)
```

### 8. Offline Support

Create `storage/LocalNotesDataSource.kt`:
```kotlin
interface LocalNotesDataSource {
    suspend fun saveNote(note: Note)
    suspend fun saveNotes(notes: List<Note>)
    suspend fun getNote(id: String): Note?
    suspend fun getNotes(): List<Note>
    suspend fun deleteNote(id: String)
    suspend fun clearAll()
}

// Implementation using platform-specific storage
expect class LocalNotesDataSourceImpl : LocalNotesDataSource
```

### 9. Network Monitoring

Create `utils/NetworkMonitor.kt`:
```kotlin
expect class NetworkMonitor {
    val isConnected: StateFlow<Boolean>
    fun startMonitoring()
    fun stopMonitoring()
}

// Use in ViewModel
class NotesViewModel(
    private val repository: NotesRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    init {
        networkMonitor.startMonitoring()
        
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                if (isConnected) {
                    syncNotes()
                }
            }
        }
    }
}
```

### 10. Error Handling

Create `utils/ApiException.kt`:
```kotlin
sealed class ApiException(message: String) : Exception(message) {
    class NetworkException(message: String = "No internet connection") : ApiException(message)
    class ServerException(message: String = "Server error") : ApiException(message)
    class UnauthorizedException(message: String = "Unauthorized") : ApiException(message)
    class NotFoundException(message: String = "Not found") : ApiException(message)
    class ValidationException(message: String = "Validation error") : ApiException(message)
}

fun handleApiError(error: Throwable): String {
    return when (error) {
        is ApiException.NetworkException -> "Check your internet connection"
        is ApiException.UnauthorizedException -> "Please login again"
        is ApiException.ServerException -> "Server error, please try again"
        else -> error.message ?: "Unknown error occurred"
    }
}
```

## 🎯 Implementation Checklist

### Backend API Requirements
- [ ] User authentication endpoints
- [ ] CRUD endpoints for notes
- [ ] Search and filter endpoints
- [ ] Real-time sync endpoint
- [ ] File upload for attachments

### Frontend Tasks
- [ ] Add Ktor and serialization dependencies
- [ ] Implement API service layer
- [ ] Add repository pattern
- [ ] Update ViewModels with coroutines
- [ ] Implement offline storage
- [ ] Add network monitoring
- [ ] Implement error handling
- [ ] Add loading states
- [ ] Add pull-to-refresh
- [ ] Implement pagination
- [ ] Add retry logic
- [ ] Implement authentication flow
- [ ] Add user settings sync

### UI Enhancements
- [ ] Loading indicators
- [ ] Error messages
- [ ] Retry buttons
- [ ] Offline indicator
- [ ] Sync status badge
- [ ] Pull to refresh
- [ ] Optimistic updates

## 📝 API Endpoints

```
POST   /api/auth/register        - Register new user
POST   /api/auth/login           - Login user
POST   /api/auth/logout          - Logout user
POST   /api/auth/refresh         - Refresh token

GET    /api/notes                - Get all notes
POST   /api/notes                - Create note
GET    /api/notes/:id            - Get note by ID
PUT    /api/notes/:id            - Update note
DELETE /api/notes/:id            - Delete note
PATCH  /api/notes/:id/pin        - Toggle pin
GET    /api/notes/search         - Search notes
POST   /api/notes/sync           - Sync notes

GET    /api/user/profile         - Get user profile
PUT    /api/user/profile         - Update profile
GET    /api/user/settings        - Get settings
PUT    /api/user/settings        - Update settings
```

## 🔒 Security Considerations

1. **Token Storage**: Use secure storage for auth tokens
2. **HTTPS Only**: All API calls over HTTPS
3. **Input Validation**: Validate all user inputs
4. **Rate Limiting**: Implement on backend
5. **Data Encryption**: Encrypt sensitive local data

## 📊 Testing Strategy

1. **Unit Tests**: Test API service methods
2. **Integration Tests**: Test repository layer
3. **UI Tests**: Test with mock API responses
4. **E2E Tests**: Test complete flows

## 🚀 Deployment

1. **Backend**: Deploy REST API
2. **Configure**: Update API URLs
3. **Test**: Test on all platforms
4. **Monitor**: Set up error tracking
5. **Analytics**: Track usage patterns

---

**Next Steps**: 
1. Set up backend API
2. Add dependencies to project
3. Implement API service layer
4. Test API integration
5. Deploy and monitor
