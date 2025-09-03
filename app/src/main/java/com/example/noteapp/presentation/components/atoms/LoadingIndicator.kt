package com.example.noteapp.presentation.components.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Reusable loading indicator component.
 * 
 * @param modifier Modifier for customizing the component
 * @param fullScreen Whether the indicator should fill the entire screen
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    fullScreen: Boolean = true
) {
    if (fullScreen) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(24.dp)
        )
    }
}
