package com.example.noteapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.usecase.DeleteNoteUseCase
import com.example.noteapp.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Notes list screen.
 * Manages UI state and business logic for displaying and managing notes.
 */
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    init {
        getNotes()
    }

    /**
     * Fetch all notes from the repository
     */
    private fun getNotes() {
        _state.update { it.copy(isLoading = true) }
        
        getNotesUseCase()
            .onEach { notes ->
                _state.update { 
                    it.copy(
                        notes = notes,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Delete a note by its ID
     */
    fun deleteNote(id: String) {
        viewModelScope.launch {
            try {
                val result = deleteNoteUseCase(id)
                result.onFailure { e ->
                    _state.update { 
                        it.copy(error = e.message ?: "Failed to delete note")
                    }
                }
                // No need to handle success case as the Flow will update automatically
            } catch (e: Exception) {
                _state.update { 
                    it.copy(error = e.message ?: "Unknown error occurred")
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
