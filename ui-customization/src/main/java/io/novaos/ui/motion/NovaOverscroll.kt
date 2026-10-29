package io.novaos.ui.motion

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.sign

/**
 * NovaOverscroll: Implements elastic rubber-banding resistance.
 * Uses a logarithmic decay for natural physical boundaries.
 */
@Composable
fun Modifier.novaOverscroll(
    orientation: Orientation = Orientation.Vertical
): Modifier {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(0f) }

    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // If we are currently in an overscroll state, consume some of the drag
                if (offset.value != 0f && source == NestedScrollSource.Drag) {
                    val delta = if (orientation == Orientation.Vertical) available.y else available.x
                    
                    // Logarithmic resistance formula
                    val resistance = 1f / (1f + ln(1f + abs(offset.value) / 100f))
                    val consumed = delta * resistance
                    
                    scope.launch { offset.snapTo(offset.value + consumed) }
                    return available // Consume all to prevent parent scrolling
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.Drag) {
                    val delta = if (orientation == Orientation.Vertical) available.y else available.x
                    if (delta != 0f) {
                        scope.launch { offset.snapTo(offset.value + delta * 0.5f) }
                        return available
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                // Snap back when the user releases
                if (offset.value != 0f) {
                    scope.launch {
                        offset.animateTo(
                            targetValue = 0f,
                            animationSpec = NovaPhysicsSpecs.SnappySpring
                        )
                    }
                    return available
                }
                return Velocity.Zero
            }
        }
    }

    return this
        .nestedScroll(connection)
        .graphicsLayer {
            if (orientation == Orientation.Vertical) {
                translationY = offset.value
            } else {
                translationX = offset.value
            }
        }
}