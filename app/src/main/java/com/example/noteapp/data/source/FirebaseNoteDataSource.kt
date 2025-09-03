package com.example.noteapp.data.source

import com.example.noteapp.data.model.NoteDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firebase implementation of the note data source.
 * Handles all Firestore operations related to notes.
 */
class FirebaseNoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val notesCollection = firestore.collection("note")
    
    /**
     * Get all notes as a Flow to observe changes
     */
    fun getNotes(): Flow<List<NoteDto>> = callbackFlow {
        val subscription = notesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            val notes = snapshot?.documents?.mapNotNull { doc ->
                val note = doc.toObject(NoteDto::class.java)
                note?.copy(id = doc.id)
            } ?: emptyList()
            
            trySend(notes)
        }
        
        awaitClose { subscription.remove() }
    }
    
    /**
     * Get a specific note by its ID
     */
    suspend fun getNoteById(id: String): NoteDto? {
        return try {
            val doc = notesCollection.document(id).get().await()
            if (doc.exists()) {
                val note = doc.toObject(NoteDto::class.java)
                note?.copy(id = doc.id)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Add a new note
     * @return ID of the newly created note
     */
    suspend fun addNote(note: NoteDto): String {
        val noteData = hashMapOf(
            "title" to note.title,
            "description" to note.description
        )
        
        return notesCollection.add(noteData).await().id
    }
    
    /**
     * Update an existing note
     * @return true if update was successful
     */
    suspend fun updateNote(note: NoteDto): Boolean {
        return try {
            val noteData = hashMapOf(
                "title" to note.title,
                "description" to note.description
            )
            
            notesCollection.document(note.id).set(noteData).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Delete a note by its ID
     * @return true if deletion was successful
     */
    suspend fun deleteNote(id: String): Boolean {
        return try {
            notesCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
