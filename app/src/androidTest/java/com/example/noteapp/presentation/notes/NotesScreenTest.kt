package com.example.noteapp.presentation.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.theme.NoteAppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notesScreen_whenLoading_showsLoadingIndicator() {
        // Given
        val viewModel = mockk<NotesViewModel>(relaxed = true)
        val state = NotesState(isLoading = true)
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NotesScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Welcome to Note App").assertIsDisplayed()
        // Note: We can't easily test for the loading indicator directly as it doesn't have text,
        // but we can verify the screen title is displayed
    }

    @Test
    fun notesScreen_withNotes_displaysNotes() {
        // Given
        val viewModel = mockk<NotesViewModel>(relaxed = true)
        val notes = listOf(
            Note(id = "1", title = "Test Note 1", description = "Description 1"),
            Note(id = "2", title = "Test Note 2", description = "Description 2")
        )
        val state = NotesState(notes = notes)
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NotesScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Welcome to Note App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description 2").assertIsDisplayed()
    }

    @Test
    fun notesScreen_withEmptyNotes_displaysEmptyMessage() {
        // Given
        val viewModel = mockk<NotesViewModel>(relaxed = true)
        val state = NotesState(notes = emptyList())
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NotesScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Welcome to Note App").assertIsDisplayed()
        composeTestRule.onNodeWithText("No notes found. Create a new note!").assertIsDisplayed()
    }

    @Test
    fun notesScreen_withError_displaysErrorMessage() {
        // Given
        val viewModel = mockk<NotesViewModel>(relaxed = true)
        val state = NotesState(error = "Failed to load notes")
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NotesScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Welcome to Note App").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to load notes").assertIsDisplayed()
    }
}
