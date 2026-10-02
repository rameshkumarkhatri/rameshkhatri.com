package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme

/**
 * Soft accent-colored glows behind the page plus a spotlight that follows the pointer
 * ([pointer] is null on touch screens, so only the fixed glows draw there).
 */
@Composable
fun AmbientBackground(pointer: Offset?, modifier: Modifier = Modifier) {
    val accent = PortfolioTheme.colors.accent
    val dark = PortfolioTheme.colors.isDark
    val glowAlpha = if (dark) 0.14f else 0.10f
    val spotAlpha = if (dark) 0.09f else 0.07f
    Canvas(modifier) {
        fun glow(center: Offset, radius: Float, alpha: Float) = drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(accent.copy(alpha = alpha), Color.Transparent),
                center = center,
                radius = radius,
            ),
            radius = radius,
            center = center,
        )
        glow(Offset(size.width * 0.85f, size.height * 0.10f), size.minDimension * 0.55f, glowAlpha)
        glow(Offset(size.width * 0.08f, size.height * 0.85f), size.minDimension * 0.45f, glowAlpha * 0.7f)
        if (pointer != null) glow(pointer, 320.dp.toPx(), spotAlpha)
    }
}
