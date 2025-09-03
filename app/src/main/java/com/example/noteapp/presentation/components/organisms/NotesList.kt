package com.example.noteapp.presentation.components.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.components.atoms.ErrorMessage
import com.example.noteapp.presentation.components.atoms.LoadingIndicator
import com.example.noteapp.presentation.components.molecules.NoteCard

/**
 * Component that displays a list of notes.
 * 
 * @param notes List of notes to display
 * @param isLoading Whether the notes are currently loading
 * @param error Error message to display, if any
 * @param onNoteClick Callback when a note is clicked
 * @param onDeleteClick Callback when delete is requested for a note
 * @param modifier Modifier for customizing the component
 */
@Composable
fun NotesList(
    notes: List<Note>,
    isLoading: Boolean,
    error: String?,
    onNoteClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                LoadingIndicator()
            }
            error != null -> {
                ErrorMessage(message = error)
            }
            notes.isEmpty() -> {
                Text(
                    text = "No notes found. Create a new note!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = notes,
                        key = { note -> note.id }
                    ) { note ->
                        NoteCard(
                            note = note,
                            onNoteClick = onNoteClick,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }
}
