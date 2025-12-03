package org.notesapp.project.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.notesapp.project.storage.AuthStorage

class NotesApiService {
    private val baseUrl = "https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net"
    private val authStorage = AuthStorage()
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
        }
    }
    
    private var authToken: String? = null
    
    init {
        // Restore token from storage on initialization
        authToken = authStorage.getToken()
    }
    
    fun setAuthToken(token: String) {
        authToken = token
        authStorage.saveToken(token)
    }
    
    fun clearAuthToken() {
        authToken = null
        authStorage.clearToken()
    }
    
    fun isLoggedIn(): Boolean {
        return authStorage.isLoggedIn()
    }
    
    fun getStoredUsername(): String? {
        return authStorage.getUsername()
    }
    
    suspend fun register(username: String, password: String): Result<TokenResponse> {
        return try {
            val response: HttpResponse = client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, password))
            }
            
            if (response.status.isSuccess()) {
                val tokenResponse: TokenResponse = response.body()
                setAuthToken(tokenResponse.access_token)
                authStorage.saveUsername(username)
                Result.success(tokenResponse)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception("Registration failed: $errorText"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(username: String, password: String): Result<TokenResponse> {
        return try {
            println("NotesApiService: Attempting login for user: $username")
            val response: HttpResponse = client.post("/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    "username=${username}&password=${password}&grant_type=password"
                )
            }
            
            println("NotesApiService: Login response status: ${response.status}")
            if (response.status.isSuccess()) {
                val tokenResponse: TokenResponse = response.body()
                setAuthToken(tokenResponse.access_token)
                authStorage.saveUsername(username)
                println("NotesApiService: Login successful, token saved")
                Result.success(tokenResponse)
            } else {
                val errorText = response.bodyAsText()
                println("NotesApiService: Login failed - $errorText")
                Result.failure(Exception("Login failed: $errorText"))
            }
        } catch (e: Exception) {
            println("NotesApiService: Login exception - ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    fun logout() {
        clearAuthToken()
    }
    
    suspend fun getNotes(): Result<List<ApiNote>> {
        return try {
            println("NotesApiService: Fetching notes with token: ${authToken?.take(10)}...")
            val response: HttpResponse = client.get("/notes") {
                authToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                    println("NotesApiService: Added Authorization header")
                } ?: println("NotesApiService: WARNING - No auth token available!")
            }
            
            println("NotesApiService: Response status: ${response.status}")
            if (response.status.isSuccess()) {
                val rawResponse = response.bodyAsText()
                println("NotesApiService: Raw response: $rawResponse")
                
                // Parse manually
                val notes: List<ApiNote> = Json.decodeFromString(rawResponse)
                println("NotesApiService: Successfully fetched ${notes.size} notes")
                Result.success(notes)
            } else {
                val errorText = response.bodyAsText()
                println("NotesApiService: Failed to fetch notes - $errorText")
                Result.failure(Exception("Failed to fetch notes: $errorText"))
            }
        } catch (e: Exception) {
            println("NotesApiService: Exception fetching notes - ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    suspend fun createNote(note: ApiNote): Result<ApiNote> {
        return try {
            val response: HttpResponse = client.post("/notes") {
                authToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
                setBody(note)
            }
            
            if (response.status.isSuccess()) {
                val createdNote: ApiNote = response.body()
                Result.success(createdNote)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception("Failed to create note: $errorText"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateNote(noteId: String, note: ApiNote): Result<ApiNote> {
        return try {
            val response: HttpResponse = client.put("/notes/$noteId") {
                authToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
                setBody(note)
            }
            
            if (response.status.isSuccess()) {
                val updatedNote: ApiNote = response.body()
                Result.success(updatedNote)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception("Failed to update note: $errorText"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            val response: HttpResponse = client.delete("/notes/$noteId") {
                authToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }
            
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception("Failed to delete note: $errorText"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        private var instance: NotesApiService? = null
        
        fun getInstance(): NotesApiService {
            return instance ?: NotesApiService().also { instance = it }
        }
    }
}
