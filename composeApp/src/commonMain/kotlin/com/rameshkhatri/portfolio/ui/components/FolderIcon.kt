package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme

/** Folder outline used on the small project cards. */
@Composable
fun FolderIcon(modifier: Modifier = Modifier) {
    val accent = PortfolioTheme.colors.accent
    Canvas(modifier.size(width = 38.dp, height = 30.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, h * 0.12f)
            lineTo(w * 0.36f, h * 0.12f)
            lineTo(w * 0.46f, h * 0.28f)
            lineTo(w, h * 0.28f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(path, accent, style = Stroke(width = 1.5.dp.toPx()))
    }
}
