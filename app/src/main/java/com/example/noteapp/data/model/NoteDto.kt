package com.example.noteapp.data.model

import com.example.noteapp.domain.model.Note

/**
 * Data Transfer Object for Note entity.
 * This is used for data layer operations and mapping to/from domain models.
 */
data class NoteDto(
    val id: String = "",
    val title: String = "",
    val description: String = ""
) {
    /**
     * Maps this DTO to a domain model
     */
    fun toDomain(): Note {
        return Note(
            id = id,
            title = title,
            description = description
        )
    }

    companion object {
        /**
         * Maps a domain model to a DTO
         */
        fun fromDomain(note: Note): NoteDto {
            return NoteDto(
                id = note.id,
                title = note.title,
                description = note.description
            )
        }
    }
}
