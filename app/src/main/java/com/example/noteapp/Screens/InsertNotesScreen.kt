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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.Models.Notes
import com.example.noteapp.navigation.Routes
import com.example.noteapp.ui.theme.colorBlack
import com.example.noteapp.ui.theme.colorGray
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertNotesScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Save the note to Firebase
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val db = FirebaseFirestore.getInstance()
                        val notesCollection = db.collection("notes")
                        
                        val note = Notes(title, description)
                        notesCollection.add(note)
                            .addOnSuccessListener {
                                // Navigate back after successful save
                                navController.navigate(Routes.NOTE_SCREEN) {
                                    popUpTo(Routes.NOTE_SCREEN) { inclusive = true }
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle failure
                                println("Error adding note: ${e.message}")
                            }
                    }
                },
                shape = CircleShape,
                containerColor = Color.Red,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Save Note",
                    modifier = Modifier.size(24.dp)
                )
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
                text = "Insert Data",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorBlack,
                    unfocusedContainerColor = colorBlack,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Enter description", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .heightIn(min = 300.dp, max = 250.dp), // 👈 grows between 100dp and 250dp
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorBlack,
                    unfocusedContainerColor = colorBlack,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                ),
                singleLine = false,   // 👈 allows multi-line input
                maxLines = Int.MAX_VALUE // 👈 so text can grow until max height
            )

        }
    }
}

@Preview
@Composable
fun InsertNotesScreenPreview() {
    InsertNotesScreen(navController = rememberNavController())
}
