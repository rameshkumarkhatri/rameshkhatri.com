package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.isDark
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Sun / moon button that flips between light and dark mode. Morphs between the two shapes. */
@Composable
fun ThemeToggle(isDark: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    val (source, hovered) = rememberHoverSource()
    val color = if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary
    // 0 = sun, 1 = moon
    val t by animateFloatAsState(if (isDark) 1f else 0f, tween(350))
    val label = if (isDark) "Switch to light mode" else "Switch to dark mode"
    Canvas(
        modifier
            .linkClick(source, onToggle)
            .semantics { role = Role.Button; contentDescription = label }
            .padding(10.dp)
            .size(22.dp),
    ) {
        val r = size.minDimension / 2
        val c = center
        val bodyR = r * (0.5f + 0.3f * t)
        val disc = Path().apply { addOval(Rect(c, bodyR)) }
        // Cut-out that slides in from the top-right to carve the crescent.
        val d = bodyR * (3f - 2.3f * t)
        val cut = Path().apply { addOval(Rect(Offset(c.x + d * 0.7f, c.y - d * 0.7f), bodyR * 0.9f)) }
        drawPath(Path.combine(PathOperation.Difference, disc, cut), color)
        // Sun rays fade and shrink into the disc as it becomes the moon.
        val rayAlpha = 1f - t
        if (rayAlpha > 0.01f) {
            val inner = bodyR * 1.45f
            val outer = inner + (r - inner) * rayAlpha
            for (i in 0 until 8) {
                val a = (i * 45.0) * PI / 180.0
                val dir = Offset(cos(a).toFloat(), sin(a).toFloat())
                drawLine(
                    color.copy(alpha = color.alpha * rayAlpha),
                    c + dir * inner,
                    c + dir * outer,
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}
