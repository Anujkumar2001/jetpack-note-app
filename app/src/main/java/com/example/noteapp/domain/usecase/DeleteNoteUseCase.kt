package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * Use case for deleting a note.
 */
class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Invoke operator allows the use case to be called as a function.
     * @param id The ID of the note to delete
     * @return Result indicating success or failure with error details
     */
    suspend operator fun invoke(id: String): Result<Boolean> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("Note ID cannot be empty"))
        }
        
        return try {
            val success = repository.deleteNote(id)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
