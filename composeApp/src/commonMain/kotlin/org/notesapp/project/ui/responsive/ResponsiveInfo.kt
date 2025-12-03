package org.notesapp.project.ui.responsive

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Screen size breakpoints for responsive design
 */
enum class ScreenSize {
    COMPACT,  // Phone (< 600dp)
    MEDIUM,   // Tablet/Foldable (600-840dp)
    EXPANDED  // Desktop/Large Tablet (> 840dp)
}

data class ResponsiveInfo(
    val screenSize: ScreenSize,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    val isCompact: Boolean get() = screenSize == ScreenSize.COMPACT
    val isMedium: Boolean get() = screenSize == ScreenSize.MEDIUM
    val isExpanded: Boolean get() = screenSize == ScreenSize.EXPANDED
    
    // Grid columns based on screen size
    val gridColumns: Int get() = when (screenSize) {
        ScreenSize.COMPACT -> 2
        ScreenSize.MEDIUM -> 3
        ScreenSize.EXPANDED -> 4
    }
    
    // Content padding
    val contentPadding: Dp get() = when (screenSize) {
        ScreenSize.COMPACT -> 16.dp
        ScreenSize.MEDIUM -> 24.dp
        ScreenSize.EXPANDED -> 32.dp
    }
    
    // Max content width for readability
    val maxContentWidth: Dp get() = when (screenSize) {
        ScreenSize.COMPACT -> Dp.Infinity
        ScreenSize.MEDIUM -> 800.dp
        ScreenSize.EXPANDED -> 1200.dp
    }
}

fun getScreenSize(width: Dp): ScreenSize {
    return when {
        width < 600.dp -> ScreenSize.COMPACT
        width < 840.dp -> ScreenSize.MEDIUM
        else -> ScreenSize.EXPANDED
    }
}
