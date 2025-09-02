package com.example.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.Screens.InsertNotesScreen
import com.example.noteapp.Screens.NoteScreen
import com.example.noteapp.Screens.SplashScreen

@Composable
fun NoteNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navHostController)
        }
        composable(Routes.NOTE_SCREEN) {
            NoteScreen(navController = navHostController)
        }
        composable(Routes.INSERT_NOTE_SCREEN) {
            InsertNotesScreen(navController = navHostController)
        }
    }
}

