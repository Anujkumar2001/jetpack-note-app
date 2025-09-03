package com.example.noteapp.presentation.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.usecase.AddNoteUseCase
import com.example.noteapp.domain.usecase.GetNoteByIdUseCase
import com.example.noteapp.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Note Detail screen.
 * Manages UI state and business logic for creating and editing notes.
 */
@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    init {
        // Check if we're editing an existing note
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            if (noteId.isNotBlank()) {
                _state.update { it.copy(isEditMode = true, id = noteId) }
                loadNote(noteId)
            }
        }
    }

    /**
     * Load an existing note by its ID
     */
    private fun loadNote(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val note = getNoteByIdUseCase(id)
                if (note != null) {
                    _state.update { 
                        it.copy(
                            title = note.title,
                            description = note.description,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "Note not found"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load note"
                    )
                }
            }
        }
    }

    /**
     * Update the title in the state
     */
    fun onTitleChanged(title: String) {
        _state.update { it.copy(title = title) }
    }

    /**
     * Update the description in the state
     */
    fun onDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    /**
     * Save the current note (create new or update existing)
     */
    fun saveNote() {
        val currentState = state.value
        
        if (currentState.title.isBlank() || currentState.description.isBlank()) {
            _state.update { 
                it.copy(error = "Title and description cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                if (currentState.isEditMode && currentState.id != null) {
                    // Update existing note
                    val result = updateNoteUseCase(
                        currentState.id,
                        currentState.title,
                        currentState.description
                    )
                    
                    result.fold(
                        onSuccess = { success ->
                            _state.update { 
                                it.copy(
                                    isLoading = false,
                                    isSaved = success,
                                    error = if (!success) "Failed to update note" else null
                                )
                            }
                        },
                        onFailure = { e ->
                            _state.update { 
                                it.copy(
                                    isLoading = false,
                                    error = e.message ?: "Failed to update note"
                                )
                            }
                        }
                    )
                } else {
                    // Create new note
                    val result = addNoteUseCase(
                        currentState.title,
                        currentState.description
                    )
                    
                    result.fold(
                        onSuccess = { id ->
                            _state.update { 
                                it.copy(
                                    isLoading = false,
                                    id = id,
                                    isSaved = true
                                )
                            }
                        },
                        onFailure = { e ->
                            _state.update { 
                                it.copy(
                                    isLoading = false,
                                    error = e.message ?: "Failed to create note"
                                )
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
