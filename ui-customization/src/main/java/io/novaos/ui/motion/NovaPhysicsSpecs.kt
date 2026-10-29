package io.novaos.ui.motion

import android.view.animation.PathInterpolator
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * NovaPhysicsSpecs: Pre-calibrated motion curves for the NovaOS ecosystem.
 */
object NovaPhysicsSpecs {

    // APPLE-INSPIRED: High elasticity and responsiveness for app transitions
    val AppLaunchSpring = spring<Float>(
        dampingRatio = 0.75f,
        stiffness = 350f
    )

    // SNAPPY: High stiffness for modal sheets and small gestures
    val SnappySpring = spring<Float>(
        dampingRatio = 0.85f,
        stiffness = 450f
    )

    // SAMSUNG-INSPIRED: Smooth spatial axis transformations
    val SpatialPathInterpolator = PathInterpolator(0.05f, 0.7f, 0.1f, 1.0f)

    /**
     * Helper to create a custom spring spec with specific initial velocity.
     */
    fun <T> springWithVelocity(
        dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        stiffness: Float = Spring.StiffnessMedium,
        visibilityThreshold: T? = null
    ): SpringSpec<T> = spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness,
        visibilityThreshold = visibilityThreshold
    )
}