package org.notesapp.project.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.notesapp.project.model.Note
import org.notesapp.project.model.NoteColor
import org.notesapp.project.network.ApiNote
import org.notesapp.project.network.NotesApiService
import org.notesapp.project.utils.currentTimeMillis

class NotesViewModel : ViewModel() {
    var notes by mutableStateOf<List<Note>>(emptyList())
        private set
    
    var searchQuery by mutableStateOf("")
        private set
    
    var selectedNote by mutableStateOf<Note?>(null)
        private set
    
    var isGridView by mutableStateOf(true)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    private val apiService = NotesApiService.getInstance()
    
    init {
        // Only load notes if user is already logged in
        if (apiService.isLoggedIn()) {
            loadNotesFromApi()
        }
    }
    
    fun loadNotesFromApi() {
        viewModelScope.launch {
            println("NotesViewModel: Starting to load notes from API...")
            isLoading = true
            errorMessage = null
            
            apiService.getNotes().fold(
                onSuccess = { apiNotes ->
                    println("NotesViewModel: Successfully loaded ${apiNotes.size} notes from API")
                    notes = apiNotes.map { it.toNote() }
                    isLoading = false
                },
                onFailure = { error ->
                    println("NotesViewModel: Failed to load notes - ${error.message}")
                    errorMessage = error.message
                    isLoading = false
                    // Don't load sample notes - show empty state instead
                }
            )
        }
    }
    fun addNote(note: Note) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val apiNote = note.toApiNote()
            apiService.createNote(apiNote).fold(
                onSuccess = { createdNote ->
                    notes = notes + createdNote.toNote()
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message
                    isLoading = false
                    // Add locally as fallback
                    notes = notes + note
                }
            )
        }
    }
    fun updateNote(note: Note) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val apiNote = note.toApiNote()
            apiService.updateNote(note.id, apiNote).fold(
                onSuccess = { updatedNote ->
                    notes = notes.map { if (it.id == note.id) updatedNote.toNote() else it }
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message
                    isLoading = false
                    // Update locally as fallback
                    notes = notes.map { if (it.id == note.id) note else it }
                }
            )
        }
    }
    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            apiService.deleteNote(noteId).fold(
                onSuccess = {
                    notes = notes.filter { it.id != noteId }
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message
                    isLoading = false
                    // Delete locally as fallback
                    notes = notes.filter { it.id != noteId }
                }
            )
        }
    }
    
    fun togglePin(noteId: String) {
        val note = notes.find { it.id == noteId } ?: return
        val updatedNote = note.copy(isPinned = !note.isPinned)
        updateNote(updatedNote)
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
    
    fun selectNote(note: Note?) {
        selectedNote = note
    }
    
    fun toggleViewMode() {
        isGridView = !isGridView
    }
    
    fun clearNotes() {
        notes = emptyList()
        searchQuery = ""
        errorMessage = null
    }
    
    fun getFilteredNotes(): List<Note> {
        val filtered = if (searchQuery.isBlank()) {
            notes
        } else {
            notes.filter { note ->
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.content.contains(searchQuery, ignoreCase = true)
            }
        }
        return filtered.sortedWith(
            compareByDescending<Note> { it.isPinned }
                .thenByDescending { it.timestamp }
        )
    }
    
    // Helper functions to convert between Note and ApiNote
    private fun Note.toApiNote(): ApiNote {
        return ApiNote(
            id = this.id.toIntOrNull(),  // Convert String ID to Int
            title = this.title,
            content = this.content,
            color = this.color.name,
            isPinned = this.isPinned,
            timestamp = this.timestamp
        )
    }
    
    private fun ApiNote.toNote(): Note {
        return Note(
            id = this.id?.toString() ?: Note.generateId(),  // Convert Int ID to String
            title = this.title,
            content = this.content,
            color = try { 
                this.color?.let { NoteColor.valueOf(it.uppercase()) } ?: NoteColor.BLUE
            } catch (e: Exception) { 
                NoteColor.BLUE 
            },
            isPinned = this.isPinned,
            timestamp = this.timestamp ?: currentTimeMillis()
        )
    }
}
