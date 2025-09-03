package com.example.noteapp.data.repository

import com.example.noteapp.data.model.NoteDto
import com.example.noteapp.data.source.FirebaseNoteDataSource
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the NoteRepository interface.
 * Acts as a mediator between the data source and the domain layer.
 */
class NoteRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseNoteDataSource
) : NoteRepository {
    
    override fun getNotes(): Flow<List<Note>> {
        return dataSource.getNotes().map { noteDtos ->
            noteDtos.map { it.toDomain() }
        }
    }
    
    override suspend fun getNoteById(id: String): Note? {
        return dataSource.getNoteById(id)?.toDomain()
    }
    
    override suspend fun addNote(note: Note): String {
        return dataSource.addNote(NoteDto.fromDomain(note))
    }
    
    override suspend fun updateNote(note: Note): Boolean {
        return dataSource.updateNote(NoteDto.fromDomain(note))
    }
    
    override suspend fun deleteNote(id: String): Boolean {
        return dataSource.deleteNote(id)
    }
}
