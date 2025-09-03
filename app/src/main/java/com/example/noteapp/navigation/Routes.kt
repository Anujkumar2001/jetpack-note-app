package com.example.noteapp.navigation

/**
 * Centralized navigation routes for the app.
 * Includes constants for route names and helper functions for building parameterized routes.
 */
object Routes {
    // Base routes
    const val SPLASH_SCREEN = "splash"
    const val NOTE_SCREEN = "note"
    const val NOTE_DETAIL_SCREEN = "note_detail"
    
    // Route with parameters
    private const val NOTE_DETAIL_WITH_ID = "note_detail/{noteId}"
    
    /**
     * Build a route to the note detail screen with a specific note ID
     * @param noteId The ID of the note to edit, or null for creating a new note
     * @return The navigation route string
     */
    fun noteDetailRoute(noteId: String? = null): String {
        return if (noteId != null) {
            NOTE_DETAIL_SCREEN.replace("{noteId}", noteId)
        } else {
            NOTE_DETAIL_SCREEN
        }
    }
    
    /**
     * Get the route pattern for note detail with ID parameter
     * Used when setting up the NavHost
     */
    fun noteDetailPattern(): String = "$NOTE_DETAIL_SCREEN/{noteId}"
}
