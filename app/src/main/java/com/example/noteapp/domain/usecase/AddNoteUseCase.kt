package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * Use case for adding a new note.
 */
class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Invoke operator allows the use case to be called as a function.
     * @param title The title of the note
     * @param description The description/content of the note
     * @return ID of the newly created note
     */
    suspend operator fun invoke(title: String, description: String): Result<String> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Title cannot be empty"))
        }
        
        if (description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description cannot be empty"))
        }
        
        return try {
            val note = Note(
                id = "", // ID will be assigned by the repository
                title = title.trim(),
                description = description.trim()
            )
            val id = repository.addNote(note)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
