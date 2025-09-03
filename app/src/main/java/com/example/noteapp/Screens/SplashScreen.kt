package com.example.noteapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.R
import com.example.noteapp.navigation.Routes
import com.example.noteapp.ui.theme.colorBlack
import com.example.noteapp.ui.theme.colorGray
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController) {
    // Navigate to the Note screen after a delay
    LaunchedEffect(key1 = true) {
        delay(2000) 
        navController.navigate(Routes.NOTE_SCREEN) {
            // Remove the splash screen from the back stack
            popUpTo(Routes.SPLASH_SCREEN) { inclusive = true }
        }
    }
    
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = colorGray),
            contentAlignment = Alignment.Center // ✅ Centers the image
        ) {
            Image(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
