package com.example.noteapp.domain.usecase

import javax.inject.Inject

/**
 * Container class that holds all note-related use cases.
 * This makes it easier to inject all use cases together where needed.
 */
data class NoteUseCases @Inject constructor(
    val getNotes: GetNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val addNote: AddNoteUseCase,
    val updateNote: UpdateNoteUseCase,
    val deleteNote: DeleteNoteUseCase
)
