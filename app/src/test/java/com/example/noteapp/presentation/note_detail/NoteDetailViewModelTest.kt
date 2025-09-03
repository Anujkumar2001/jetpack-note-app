package com.example.noteapp.presentation.note_detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.usecase.AddNoteUseCase
import com.example.noteapp.domain.usecase.GetNoteByIdUseCase
import com.example.noteapp.domain.usecase.UpdateNoteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class NoteDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: NoteDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getNoteByIdUseCase = mockk()
        addNoteUseCase = mockk()
        updateNoteUseCase = mockk()
        savedStateHandle = SavedStateHandle()
        
        // Default mock behavior
        coEvery { getNoteByIdUseCase(any()) } returns null
        coEvery { addNoteUseCase(any(), any()) } returns Result.success("new-id")
        coEvery { updateNoteUseCase(any(), any(), any()) } returns Result.success(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init with noteId loads note and sets edit mode`() = runTest {
        // Given
        val noteId = "note-id"
        val note = Note(id = noteId, title = "Test Note", description = "Test Description")
        savedStateHandle["noteId"] = noteId
        coEvery { getNoteByIdUseCase(noteId) } returns note

        // When
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.id).isEqualTo(noteId)
            assertThat(state.title).isEqualTo(note.title)
            assertThat(state.description).isEqualTo(note.description)
            assertThat(state.isEditMode).isTrue()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `init without noteId sets create mode`() = runTest {
        // When
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.id).isNull()
            assertThat(state.isEditMode).isFalse()
        }
    }

    @Test
    fun `onTitleChanged updates title in state`() = runTest {
        // Given
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        val newTitle = "New Title"

        // When
        viewModel.onTitleChanged(newTitle)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.title).isEqualTo(newTitle)
        }
    }

    @Test
    fun `onDescriptionChanged updates description in state`() = runTest {
        // Given
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        val newDescription = "New Description"

        // When
        viewModel.onDescriptionChanged(newDescription)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.description).isEqualTo(newDescription)
        }
    }

    @Test
    fun `saveNote in create mode calls addNoteUseCase`() = runTest {
        // Given
        val title = "New Note"
        val description = "New Description"
        val newId = "new-note-id"
        
        coEvery { addNoteUseCase(title, description) } returns Result.success(newId)
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        
        viewModel.onTitleChanged(title)
        viewModel.onDescriptionChanged(description)

        // When
        viewModel.saveNote()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { addNoteUseCase(title, description) }
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.id).isEqualTo(newId)
            assertThat(state.isSaved).isTrue()
        }
    }

    @Test
    fun `saveNote in edit mode calls updateNoteUseCase`() = runTest {
        // Given
        val noteId = "note-id"
        val title = "Updated Note"
        val description = "Updated Description"
        
        savedStateHandle["noteId"] = noteId
        coEvery { updateNoteUseCase(noteId, title, description) } returns Result.success(true)
        
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        viewModel.onTitleChanged(title)
        viewModel.onDescriptionChanged(description)

        // When
        viewModel.saveNote()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { updateNoteUseCase(noteId, title, description) }
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isSaved).isTrue()
        }
    }

    @Test
    fun `saveNote with empty fields shows error`() = runTest {
        // Given
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        viewModel.onTitleChanged("")
        viewModel.onDescriptionChanged("")

        // When
        viewModel.saveNote()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNotNull()
            assertThat(state.isSaved).isFalse()
        }
    }

    @Test
    fun `clearError removes error from state`() = runTest {
        // Given
        viewModel = NoteDetailViewModel(getNoteByIdUseCase, addNoteUseCase, updateNoteUseCase, savedStateHandle)
        viewModel.saveNote() // This will generate an error due to empty fields

        // When
        viewModel.clearError()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNull()
        }
    }
}
