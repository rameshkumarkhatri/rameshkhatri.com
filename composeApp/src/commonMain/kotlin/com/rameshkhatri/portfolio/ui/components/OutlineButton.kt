package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.utils.animateLift
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun OutlineButton(text: String, big: Boolean = false, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    val shape = RoundedCornerShape(4.dp)
    val accent = PortfolioTheme.colors.accent
    val lift = animateLift(hovered, 3.dp)
    val fill by animateColorAsState(if (hovered) accent.copy(alpha = 0.12f) else Color.Transparent, tween(200))
    val glow by animateDpAsState(if (hovered) 14.dp else 0.dp, tween(200))
    Box(
        Modifier
            .graphicsLayer { translationY = lift.toPx() }
            .shadow(glow, shape, ambientColor = accent, spotColor = accent)
            .clip(shape)
            .border(1.dp, accent, shape)
            .background(fill)
            .linkClick(source, onClick)
            .padding(horizontal = if (big) 28.dp else 16.dp, vertical = if (big) 18.dp else 10.dp),
    ) {
        Text(text, style = mono(if (big) 14.sp else 13.sp))
    }
}
