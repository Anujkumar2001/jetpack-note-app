package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class UpdateNoteUseCaseTest {
    
    private lateinit var noteRepository: NoteRepository
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    
    @Before
    fun setUp() {
        noteRepository = mockk()
        updateNoteUseCase = UpdateNoteUseCase(noteRepository)
    }
    
    @Test
    fun `update note with valid input returns success`() = runBlocking {
        // Given
        val id = "note-id"
        val title = "Updated Title"
        val description = "Updated Description"
        
        coEvery { noteRepository.updateNote(any()) } returns true
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
    }
    
    @Test
    fun `update note with empty id returns failure`() = runBlocking {
        // Given
        val id = ""
        val title = "Updated Title"
        val description = "Updated Description"
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("ID cannot be empty")
    }
    
    @Test
    fun `update note with empty title returns failure`() = runBlocking {
        // Given
        val id = "note-id"
        val title = ""
        val description = "Updated Description"
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Title cannot be empty")
    }
    
    @Test
    fun `update note with empty description returns failure`() = runBlocking {
        // Given
        val id = "note-id"
        val title = "Updated Title"
        val description = ""
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Description cannot be empty")
    }
    
    @Test
    fun `update note with repository failure returns failure result`() = runBlocking {
        // Given
        val id = "note-id"
        val title = "Updated Title"
        val description = "Updated Description"
        
        coEvery { noteRepository.updateNote(any()) } returns false
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isFalse()
    }
    
    @Test
    fun `update note with repository exception returns failure`() = runBlocking {
        // Given
        val id = "note-id"
        val title = "Updated Title"
        val description = "Updated Description"
        val exception = RuntimeException("Repository error")
        
        coEvery { noteRepository.updateNote(any()) } throws exception
        
        // When
        val result = updateNoteUseCase(id, title, description)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}
