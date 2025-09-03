package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * Use case for retrieving a specific note by its ID.
 */
class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Invoke operator allows the use case to be called as a function.
     * @param id The ID of the note to retrieve
     * @return The note if found, null otherwise
     */
    suspend operator fun invoke(id: String): Note? {
        return repository.getNoteById(id)
    }
}
