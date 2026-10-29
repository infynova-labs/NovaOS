package io.novaos.ui.motion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NovaMotionEngine: The central orchestration unit for OS-wide physics and transformations.
 */
@Singleton
class NovaMotionEngine @Inject constructor() {

    companion object {
        @JvmStatic
        val INSTANCE: NovaMotionEngine by lazy { NovaMotionEngine() }
        @JvmStatic
        fun getInstance() = INSTANCE
    }

    // Velocity Tracking for Interruptible Animations
    private var lastVelocity = 0f

    // Stiffness property for system-wide physics integration
    private val mStiffness = Animatable(Spring.StiffnessMedium)

    /**
     * Returns the current stiffness Animatable object for Java/Window service bindings.
     */
    fun getStiffness(): Animatable<Float, AnimationVector1D> {
        return mStiffness
    }

    /**
     * Captures current animation velocity to allow seamless interruption.
     */
    fun captureVelocity(animatable: Animatable<Float, AnimationVector1D>) {
        lastVelocity = animatable.velocity
    }

    /**
     * Returns a spring spec for App Launch/Exit, incorporating Apple-style high elasticity.
     */
    fun appTransitionSpec() = NovaPhysicsSpecs.AppLaunchSpring

    /**
     * Focal Zoom: Calculates the scale and offset for an app expansion.
     * Starts from the physical [iconPosition] on the home screen.
     */
    fun calculateFocalZoom(
        iconPosition: Offset,
        screenSize: Offset,
        progress: Float
    ): FocalTransform {
        val scale = 0.1f + (progress * 0.9f)
        val offsetX = (iconPosition.x - (screenSize.x / 2)) * (1f - progress)
        val offsetY = (iconPosition.y - (screenSize.y / 2)) * (1f - progress)
        
        return FocalTransform(scale, Offset(offsetX, offsetY))
    }

    data class FocalTransform(
        val scale: Float,
        val translation: Offset
    )
}