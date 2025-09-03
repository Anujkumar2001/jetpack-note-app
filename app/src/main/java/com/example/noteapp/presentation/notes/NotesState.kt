package com.example.noteapp.presentation.notes

import com.example.noteapp.domain.model.Note

/**
 * UI state for the Notes screen.
 * Represents all possible states of the UI.
 */
data class NotesState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
