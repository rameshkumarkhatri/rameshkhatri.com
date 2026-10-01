package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun OutlineButton(text: String, big: Boolean = false, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    val shape = RoundedCornerShape(4.dp)
    Box(
        Modifier
            .clip(shape)
            .border(1.dp, PortfolioTheme.colors.accent, shape)
            .background(if (hovered) PortfolioTheme.colors.accent.copy(alpha = 0.1f) else Color.Transparent)
            .linkClick(source, onClick)
            .padding(horizontal = if (big) 28.dp else 16.dp, vertical = if (big) 18.dp else 10.dp),
    ) {
        Text(text, style = mono(if (big) 14.sp else 13.sp))
    }
}
