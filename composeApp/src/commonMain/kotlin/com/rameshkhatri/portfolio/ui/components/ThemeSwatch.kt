package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.ThemeSpec
import com.rameshkhatri.portfolio.designsystem.isDark

/** Round preview of a theme: its background with a half-disc of its accent. Ringed when [selected]. */
@Composable
fun ThemeSwatch(
    spec: ThemeSpec,
    isDark: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
) {
    val preview = spec.colors(isDark)
    val ring = PortfolioTheme.colors.accent
    val edge = PortfolioTheme.colors.outline
    Canvas(
        modifier
            .semantics { role = Role.Button; contentDescription = "${spec.name} theme" }
            .padding(4.dp)
            .size(size),
    ) {
        val r = this.size.minDimension / 2
        val inner = r - 4.dp.toPx()
        drawCircle(preview.background, inner)
        drawArc(
            color = preview.accent,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(center.x - inner, center.y - inner),
            size = Size(inner * 2, inner * 2),
        )
        drawCircle(edge, inner, style = Stroke(1.dp.toPx()))
        if (selected) drawCircle(ring, r - 1.dp.toPx(), style = Stroke(2.dp.toPx()))
    }
}
