package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * Use case for updating an existing note.
 */
class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Invoke operator allows the use case to be called as a function.
     * @param id The ID of the note to update
     * @param title The updated title
     * @param description The updated description
     * @return Result indicating success or failure with error details
     */
    suspend operator fun invoke(id: String, title: String, description: String): Result<Boolean> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("Note ID cannot be empty"))
        }
        
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Title cannot be empty"))
        }
        
        if (description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description cannot be empty"))
        }
        
        return try {
            val note = Note(
                id = id,
                title = title.trim(),
                description = description.trim()
            )
            val success = repository.updateNote(note)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
