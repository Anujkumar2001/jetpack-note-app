package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class AddNoteUseCaseTest {
    
    private lateinit var noteRepository: NoteRepository
    private lateinit var addNoteUseCase: AddNoteUseCase
    
    @Before
    fun setUp() {
        noteRepository = mockk()
        addNoteUseCase = AddNoteUseCase(noteRepository)
    }
    
    @Test
    fun `add note with valid input returns success with id`() = runBlocking {
        // Given
        val title = "Test Title"
        val description = "Test Description"
        val noteId = "new-note-id"
        
        coEvery { noteRepository.addNote(any()) } returns noteId
        
        // When
        val result = addNoteUseCase(title, description)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(noteId)
    }
    
    @Test
    fun `add note with empty title returns failure`() = runBlocking {
        // Given
        val title = ""
        val description = "Test Description"
        
        // When
        val result = addNoteUseCase(title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Title cannot be empty")
    }
    
    @Test
    fun `add note with empty description returns failure`() = runBlocking {
        // Given
        val title = "Test Title"
        val description = ""
        
        // When
        val result = addNoteUseCase(title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Description cannot be empty")
    }
    
    @Test
    fun `add note with repository exception returns failure`() = runBlocking {
        // Given
        val title = "Test Title"
        val description = "Test Description"
        val exception = RuntimeException("Repository error")
        
        coEvery { noteRepository.addNote(any()) } throws exception
        
        // When
        val result = addNoteUseCase(title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}
