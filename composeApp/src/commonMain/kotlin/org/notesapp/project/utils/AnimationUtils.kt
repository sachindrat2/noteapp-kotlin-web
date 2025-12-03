package org.notesapp.project.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

/**
 * Creates a pulsing animation effect
 */
@Composable
fun rememberPulseAnimation(
    duration: Int = 1000,
    minScale: Float = 0.95f,
    maxScale: Float = 1.05f
): Float {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    ).value
}

/**
 * Creates a shimmer loading effect
 */
@Composable
fun rememberShimmerAnimation(): Float {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    ).value
}

/**
 * Creates a bounce animation
 */
fun bounceAnimation(
    dampingRatio: Float = Spring.DampingRatioMediumBouncy,
    stiffness: Float = Spring.StiffnessMedium
): SpringSpec<Float> = spring(
    dampingRatio = dampingRatio,
    stiffness = stiffness
)

/**
 * Standard spring animation for UI elements
 */
fun standardSpring(): SpringSpec<Float> = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessMedium
)

/**
 * Fast spring animation for quick interactions
 */
fun fastSpring(): SpringSpec<Float> = spring(
    dampingRatio = Spring.DampingRatioLowBouncy,
    stiffness = Spring.StiffnessHigh
)

/**
 * Gentle spring animation for smooth transitions
 */
fun gentleSpring(): SpringSpec<Float> = spring(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessLow
)

/**
 * Delayed animation effect
 */
@Composable
fun rememberDelayedVisibility(
    delayMillis: Long = 300
): Boolean {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(delayMillis)
        visible = true
    }
    
    return visible
}
