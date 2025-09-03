package com.example.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.noteapp.Screens.SplashScreen
import com.example.noteapp.presentation.note_detail.NoteDetailScreen
import com.example.noteapp.presentation.note_detail.NoteDetailViewModel
import com.example.noteapp.presentation.notes.NotesScreen

/**
 * Main navigation component for the app.
 * Sets up the navigation graph with all screens and their arguments.
 */
@Composable
fun NoteNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        // Splash screen
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navHostController)
        }
        
        // Notes list screen
        composable(Routes.NOTE_SCREEN) {
            NotesScreen(navController = navHostController)
        }
        
        // Note detail screen (create new note)
        composable(Routes.NOTE_DETAIL_SCREEN) {
            NoteDetailScreen(navController = navHostController)
        }
        
        // Note detail screen (edit existing note)
        composable(
            route = Routes.noteDetailPattern(),
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                }
            )
        ) {
            NoteDetailScreen(navController = navHostController)
        }
    }
}
