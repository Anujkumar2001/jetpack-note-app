package com.example.noteapp.Models

data class Notes(
    val title: String = "",  // Default values for Firebase deserialization
    val description: String = ""
)
