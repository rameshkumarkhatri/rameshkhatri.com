package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme

@Composable
fun Bullet(topPadding: Dp, modifier: Modifier = Modifier) {
    val accent = PortfolioTheme.colors.accent
    Canvas(modifier.padding(top = topPadding).size(width = 7.dp, height = 9.dp)) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, size.height / 2)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, accent)
    }
}

@Composable
fun BulletText(text: String, style: TextStyle, modifier: Modifier = Modifier) {
    val line = style.lineHeight.value.takeIf { !it.isNaN() } ?: (style.fontSize.value * 1.4f)
    Row(modifier) {
        Bullet(topPadding = ((line - 9f) / 2f).coerceAtLeast(0f).dp)
        Spacer(Modifier.width(14.dp))
        Text(text, style = style)
    }
}
