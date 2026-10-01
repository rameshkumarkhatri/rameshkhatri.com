package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme

/** Three-line menu icon that turns into an X when [open]. */
@Composable
fun MenuIcon(open: Boolean, modifier: Modifier = Modifier) {
    val t by animateFloatAsState(if (open) 1f else 0f, tween(250))
    val accent = PortfolioTheme.colors.accent
    Canvas(modifier.size(width = 30.dp, height = 24.dp)) {
        val stroke = 2.dp.toPx()
        val w = size.width
        val h = size.height
        val mid = h / 2
        if (t < 0.5f) {
            val k = 1f - t * 2f
            drawLine(accent, Offset(0f, mid - (mid - stroke) * k), Offset(w, mid - (mid - stroke) * k), stroke, StrokeCap.Round)
            drawLine(accent, Offset(w * 0.2f, mid), Offset(w, mid), stroke, StrokeCap.Round)
            drawLine(accent, Offset(w * 0.4f, mid + (mid - stroke) * k), Offset(w, mid + (mid - stroke) * k), stroke, StrokeCap.Round)
        } else {
            val k = (t - 0.5f) * 2f
            val dy = (w / 2) * k * 0.8f
            drawLine(accent, Offset(w * 0.1f, mid - dy), Offset(w * 0.9f, mid + dy), stroke, StrokeCap.Round)
            drawLine(accent, Offset(w * 0.1f, mid + dy), Offset(w * 0.9f, mid - dy), stroke, StrokeCap.Round)
        }
    }
}
