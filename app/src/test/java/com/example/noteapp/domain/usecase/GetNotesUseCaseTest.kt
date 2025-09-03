package com.example.noteapp.domain.usecase

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class GetNotesUseCaseTest {
    
    private lateinit var noteRepository: NoteRepository
    private lateinit var getNotesUseCase: GetNotesUseCase
    
    @Before
    fun setUp() {
        noteRepository = mockk()
        getNotesUseCase = GetNotesUseCase(noteRepository)
    }
    
    @Test
    fun `get notes returns flow from repository`() = runBlocking {
        // Given
        val notes = listOf(
            Note(id = "1", title = "Test 1", description = "Description 1"),
            Note(id = "2", title = "Test 2", description = "Description 2")
        )
        coEvery { noteRepository.getNotes() } returns flowOf(notes)
        
        // When
        val result = getNotesUseCase().first()
        
        // Then
        assertThat(result).isEqualTo(notes)
    }
    
    @Test
    fun `get notes returns empty list when repository returns empty`() = runBlocking {
        // Given
        coEvery { noteRepository.getNotes() } returns flowOf(emptyList())
        
        // When
        val result = getNotesUseCase().first()
        
        // Then
        assertThat(result).isEmpty()
    }
}
