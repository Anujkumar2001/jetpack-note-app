package com.example.noteapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.Models.Notes
import com.example.noteapp.navigation.Routes
import com.example.noteapp.ui.theme.colorBlack
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NoteScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val notesCollection = db.collection("note")
    val notesList = remember { mutableStateListOf<Notes>() }
    val loading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        notesCollection.addSnapshotListener { value, error ->
            loading.value = true
            if (error == null) {
                notesList.clear()
                value?.documents?.forEach { doc ->
                    val note = doc.toObject(Notes::class.java)
                    note?.id = doc.id
                    if (note != null) {
                        notesList.add(note)
                    }
                }
                loading.value = false
            } else {
                notesList.clear()
                loading.value = false
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.INSERT_NOTE_SCREEN)
                },
                shape = CircleShape,
                containerColor = Color.Red,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorBlack)
                .padding(10.dp)
        ) {
            Text(
                text = "Welcome to Note App",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            if (loading.value) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(notesList) { note ->
                    ListItem(note, navController)
                }
            }
        }
    }
}

@Composable
fun ListItem(note: Notes, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val db = FirebaseFirestore.getInstance()
    val dbRef = db.collection("note")

    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.DarkGray)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        // Menu
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Update") },
                    onClick = {
                        expanded = false
                        navController.navigate("${Routes.INSERT_NOTE_SCREEN}/${note.id}")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        dbRef.document(note.id).delete()
                        expanded = false
                    }
                )
            }
        }

        Column {
            Text(text = note.title, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.description, color = Color.LightGray)
        }
    }
}
