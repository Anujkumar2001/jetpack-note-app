package com.example.noteapp.presentation.note_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteapp.navigation.Routes
import com.example.noteapp.presentation.components.atoms.ErrorMessage
import com.example.noteapp.presentation.components.atoms.LoadingIndicator
import com.example.noteapp.presentation.components.atoms.NoteTextField
import com.example.noteapp.ui.theme.colorBlack

/**
 * Screen for creating or editing a note.
 * Uses NoteDetailViewModel to manage state and business logic.
 */
@Composable
fun NoteDetailScreen(
    navController: NavController,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Navigate back when note is saved
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.navigate(Routes.NOTE_SCREEN) {
                popUpTo(Routes.NOTE_SCREEN) { inclusive = true }
            }
        }
    }
    
    // Show error messages in a snackbar
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(message = error)
            viewModel.clearError()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorBlack)
                .padding(16.dp)
        ) {
            Text(
                text = if (state.isEditMode) "Update Note" else "Create Note",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (state.isLoading) {
                LoadingIndicator()
            } else {
                state.error?.let { error ->
                    ErrorMessage(message = error)
                }
                
                NoteTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChanged,
                    label = "Title",
                    singleLine = true
                )
                
                NoteTextField(
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    label = "Description",
                    maxLines = Int.MAX_VALUE,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
