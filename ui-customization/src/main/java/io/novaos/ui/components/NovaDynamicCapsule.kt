package io.novaos.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.novaos.ui.motion.NovaPhysicsSpecs

/**
 * NovaDynamicCapsule: The interactive status bar Hero.
 * Transitions between Idle, Active, and Expanded states with spring physics.
 */
@Composable
fun NovaDynamicCapsule(
    activeTask: String? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black)
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isExpanded) {
                // Idle / Minimized State
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (activeTask != null) Color.Green else Color.Gray)
                )
                if (activeTask != null) {
                    Text(
                        text = activeTask,
                        color = Color.White,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            } else {
                // Expanded State: Media Controls or Task Details
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = activeTask ?: "System Idle",
                        color = Color.White,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "NovaOS Live Activity",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}