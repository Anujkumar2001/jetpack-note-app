package com.example.noteapp.presentation.note_detail

/**
 * UI state for the Note Detail/Edit screen.
 * Represents all possible states of the UI.
 */
data class NoteDetailState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false
)
