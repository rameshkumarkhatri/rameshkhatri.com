package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.mono
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Hexagon "R" logo from the original site. */
@Composable
fun Logo(initial: String, modifier: Modifier = Modifier, size: Dp = 42.dp) {
    val accent = PortfolioTheme.colors.accent
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size)) {
            val r = this.size.minDimension / 2 - 2.dp.toPx()
            val c = center
            val path = Path()
            for (i in 0 until 6) {
                val a = (60.0 * i - 90.0) * PI / 180.0
                val p = Offset(c.x + r * cos(a).toFloat(), c.y + r * sin(a).toFloat())
                if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            path.close()
            drawPath(path, accent, style = Stroke(width = 2.dp.toPx()))
        }
        Text(initial, style = mono((size.value * 0.42f).sp).copy(fontWeight = FontWeight.Bold))
    }
}
