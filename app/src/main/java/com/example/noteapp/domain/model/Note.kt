package com.example.noteapp.domain.model

/**
 * Domain model representing a Note entity.
 * This is the core business model used across the application.
 */
data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = ""
)
