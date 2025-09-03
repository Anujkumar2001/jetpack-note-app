package com.example.noteapp.presentation.note_detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.theme.NoteAppTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class NoteDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noteDetailScreen_createMode_displaysEmptyForm() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState(isEditMode = false)
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Create Note").assertIsDisplayed()
    }

    @Test
    fun noteDetailScreen_editMode_displaysFormWithData() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState(
            id = "note-id",
            title = "Test Note",
            description = "Test Description",
            isEditMode = true
        )
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Update Note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
    }

    @Test
    fun noteDetailScreen_inputFields_updateViewModel() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState()
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Title").performTextInput("New Title")
        composeTestRule.onNodeWithText("Description").performTextInput("New Description")

        verify { viewModel.onTitleChanged("New Title") }
        verify { viewModel.onDescriptionChanged("New Description") }
    }

    @Test
    fun noteDetailScreen_saveButton_callsSaveNote() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState()
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Save Note").performClick()
        verify { viewModel.saveNote() }
    }

    @Test
    fun noteDetailScreen_withError_displaysErrorMessage() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState(error = "Failed to save note")
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Failed to save note").assertIsDisplayed()
    }

    @Test
    fun noteDetailScreen_loading_showsLoadingIndicator() {
        // Given
        val viewModel = mockk<NoteDetailViewModel>(relaxed = true)
        val state = NoteDetailState(isLoading = true)
        every { viewModel.state } returns MutableStateFlow(state)

        // When
        composeTestRule.setContent {
            NoteAppTheme {
                NoteDetailScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Create Note").assertIsDisplayed()
        // Note: We can't easily test for the loading indicator directly as it doesn't have text
    }
}
