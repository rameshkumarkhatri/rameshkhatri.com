package com.rameshkhatri.portfolio.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun LinkText(
    text: String,
    url: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    hoverColor: Color = PortfolioTheme.colors.accent,
) {
    val uri = LocalUriHandler.current
    val (source, hovered) = rememberHoverSource()
    Text(
        text = text,
        style = style.copy(
            color = if (hovered) hoverColor else style.color,
            textDecoration = if (hovered) TextDecoration.Underline else TextDecoration.None,
        ),
        modifier = modifier.linkClick(source) { uri.openUri(url) },
    )
}
