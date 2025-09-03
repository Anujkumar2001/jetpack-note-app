package com.example.noteapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun NoteScreen(navController: NavController) {
    val db= FirebaseFirestore.getInstance()
    val notes=db.collection("note")
    val notesList= remember{mutableStateListOf<Notes>()}
    val loading = remember{ mutableStateOf(true) }

    LaunchedEffect(Unit) {
        notes.addSnapshotListener { value, error ->
            loading.value = true
            if (error == null) {
                val data = value?.toObjects(Notes::class.java)
                notesList.clear() // Clear the list before adding new data
                if (data != null) { // Check if data is NOT null
                    notesList.addAll(data)
                    loading.value = false
                }
            } else {
                // Handle the error case
                notesList.clear() // Optionally clear the list on error too
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
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp,
                    hoveredElevation = 8.dp,
                    focusedElevation = 8.dp
                ),
                modifier = Modifier.padding(16.dp) // Adds some margin around the FAB
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Note",
                    modifier = Modifier.size(24.dp) // Example: Set a specific size for the icon
                )
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
                text = "welcome to note app",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
           if(loading.value){
               Box(modifier = Modifier.fillMaxSize()){
                   CircularProgressIndicator(
                       modifier = Modifier.align(Alignment.Center),
                       color = Color.White
                   )
               }
           }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(notesList) { note ->
                    ListItem(notes = note)
                }
            }
        }
    }
}

@Preview
@Composable
fun NoteScreenPreview() {
    NoteScreen(navController = rememberNavController())
}

@Composable
fun ListItem(notes: Notes) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.DarkGray)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menu", // Changed "Add" to "Menu" for MoreVert icon
            tint = Color.White,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Column {
            Text(text = notes.title, color = Color.White, fontWeight = FontWeight.Bold)

            // To add a gap of, for example, 8.dp between the title and description:
            Spacer(modifier = Modifier.height(8.dp)) // Set the desired height for the gap

            Text(text = notes.description, color = Color.LightGray)
        }
    }
}