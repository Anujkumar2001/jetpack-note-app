package com.example.noteapp.domain.repository

import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Note operations.
 * This defines the contract for data operations without exposing the implementation details.
 */
interface NoteRepository {
    /**
     * Get all notes as a Flow to observe changes
     */
    fun getNotes(): Flow<List<Note>>
    
    /**
     * Get a specific note by its ID
     */
    suspend fun getNoteById(id: String): Note?
    
    /**
     * Add a new note
     * @return ID of the newly created note
     */
    suspend fun addNote(note: Note): String
    
    /**
     * Update an existing note
     * @return true if update was successful
     */
    suspend fun updateNote(note: Note): Boolean
    
    /**
     * Delete a note by its ID
     * @return true if deletion was successful
     */
    suspend fun deleteNote(id: String): Boolean
}
