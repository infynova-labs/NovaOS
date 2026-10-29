package com.infynova.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.novaos.ui.components.NovaDynamicCapsule
import io.novaos.ui.theme.SuperellipseShape

/**
 * NovaHomeScreen: The mandatory Launcher core.
 * Renders the app grid with Superellipse icons and handles system-wide gestures.
 */
@Composable
fun NovaHomeScreen(
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { /* Determine swipe direction and trigger callbacks */ },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (dragAmount.y < -50) onSwipeUp()
                        if (dragAmount.y > 50) onSwipeDown()
                    }
                )
            }
    ) {
        // USP 1: Dynamic Capsule integration
        NovaDynamicCapsule(activeTask = "Privacy Shield Active")

        Spacer(modifier = Modifier.height(48.dp))

        // App Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(16) { index ->
                AppIcon(label = "App $index")
            }
        }
        
        // Dock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(4) { AppIcon(label = "D", isDock = true) }
        }
    }
}

@Composable
fun AppIcon(label: String, isDock: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isDock) 56.dp else 64.dp)
                .clip(SuperellipseShape(n = 3f))
                .background(Color(0xFF007FAC))
        )
        if (!isDock) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

