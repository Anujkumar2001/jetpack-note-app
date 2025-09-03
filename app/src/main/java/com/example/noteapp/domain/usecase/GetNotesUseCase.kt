package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all notes.
 * Follows the single responsibility principle by focusing on one operation.
 */
class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Invoke operator allows the use case to be called as a function.
     * @return Flow of notes list that emits whenever the data changes
     */
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes()
    }
}
