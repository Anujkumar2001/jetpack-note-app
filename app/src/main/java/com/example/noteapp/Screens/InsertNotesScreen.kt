package com.example.noteapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import com.example.noteapp.navigation.Routes
import com.example.noteapp.ui.theme.colorBlack
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertNotesScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val noteId = backStackEntry.arguments?.getString("noteId") // will be null for new note

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isUpdate by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()
    val notesCollection = db.collection("note")

    // If editing, fetch note data
    LaunchedEffect(noteId) {
        if (noteId != null) {
            isUpdate = true
            notesCollection.document(noteId).get().addOnSuccessListener { doc ->
                title = doc.getString("title") ?: ""
                description = doc.getString("description") ?: ""
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val note = hashMapOf(
                            "title" to title,
                            "description" to description
                        )

                        if (isUpdate && noteId != null) {
                            // Update existing note
                            notesCollection.document(noteId).set(note)
                                .addOnSuccessListener {
                                    navController.navigate(Routes.NOTE_SCREEN) {
                                        popUpTo(Routes.NOTE_SCREEN) { inclusive = true }
                                    }
                                }
                        } else {
                            // Add new note
                            notesCollection.add(note)
                                .addOnSuccessListener {
                                    navController.navigate(Routes.NOTE_SCREEN) {
                                        popUpTo(Routes.NOTE_SCREEN) { inclusive = true }
                                    }
                                }
                        }
                    }
                },
                shape = CircleShape,
                containerColor = Color.Red,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorBlack)
                .padding(16.dp)
        ) {
            Text(
                text = if (isUpdate) "Update Note" else "Insert Note",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorBlack,
                    unfocusedContainerColor = colorBlack,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).heightIn(min = 100.dp),
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorBlack,
                    unfocusedContainerColor = colorBlack,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                singleLine = false,
                maxLines = Int.MAX_VALUE
            )
        }
    }
}
