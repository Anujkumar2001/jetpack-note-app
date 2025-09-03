package com.example.noteapp.presentation.notes

import app.cash.turbine.test
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.usecase.DeleteNoteUseCase
import com.example.noteapp.domain.usecase.GetNotesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class NotesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var viewModel: NotesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getNotesUseCase = mockk()
        deleteNoteUseCase = mockk()
        
        // Default mock behavior
        coEvery { getNotesUseCase() } returns flowOf(emptyList())
        coEvery { deleteNoteUseCase(any()) } returns Result.success(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init fetches notes and updates state`() = runTest {
        // Given
        val notes = listOf(
            Note(id = "1", title = "Note 1", description = "Description 1"),
            Note(id = "2", title = "Note 2", description = "Description 2")
        )
        coEvery { getNotesUseCase() } returns flowOf(notes)

        // When
        viewModel = NotesViewModel(getNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.notes).isEqualTo(notes)
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `deleteNote calls use case and handles success`() = runTest {
        // Given
        val noteId = "note-id"
        coEvery { deleteNoteUseCase(noteId) } returns Result.success(true)
        viewModel = NotesViewModel(getNotesUseCase, deleteNoteUseCase)

        // When
        viewModel.deleteNote(noteId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deleteNoteUseCase(noteId) }
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `deleteNote handles failure`() = runTest {
        // Given
        val noteId = "note-id"
        val errorMessage = "Failed to delete note"
        coEvery { deleteNoteUseCase(noteId) } returns Result.failure(RuntimeException(errorMessage))
        viewModel = NotesViewModel(getNotesUseCase, deleteNoteUseCase)

        // When
        viewModel.deleteNote(noteId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deleteNoteUseCase(noteId) }
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isEqualTo(errorMessage)
        }
    }

    @Test
    fun `clearError removes error from state`() = runTest {
        // Given
        viewModel = NotesViewModel(getNotesUseCase, deleteNoteUseCase)
        viewModel.state.test {
            val initialState = awaitItem()
            
            // Set an error
            val stateWithError = initialState.copy(error = "Some error")
            viewModel = NotesViewModel(getNotesUseCase, deleteNoteUseCase)
            viewModel.state.value = stateWithError
            
            // When
            viewModel.clearError()
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Then
            val updatedState = awaitItem()
            assertThat(updatedState.error).isNull()
        }
    }
}
