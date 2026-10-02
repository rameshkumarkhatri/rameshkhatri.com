package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Header
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.body
import com.rameshkhatri.portfolio.designsystem.heading
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.OutlineButton
import com.rameshkhatri.portfolio.ui.components.ParallaxLogos
import com.rameshkhatri.portfolio.ui.components.Reveal

@Composable
fun HeroSection(
    header: Header,
    contentWidth: Dp,
    minHeight: Dp,
    topPadding: Dp,
    desktop: Boolean,
    scroll: () -> Int,
    pointer: () -> Offset?,
    onGetInTouch: () -> Unit,
) {
    val big = (contentWidth.value * 0.08f).coerceIn(40f, 80f).sp
    // Fills the first screen; [topPadding] keeps the content clear of the overlaid nav bar.
    Box(Modifier.widthIn(max = 1000.dp).fillMaxWidth().padding(top = topPadding)) {
        ParallaxLogos(scroll, pointer, desktop, Modifier.matchParentSize())
        HeroText(header, big, minHeight - topPadding, onGetInTouch)
    }
}

@Composable
private fun HeroText(header: Header, big: TextUnit, minHeight: Dp, onGetInTouch: () -> Unit) {
    // The scroll parent is unbounded, so the column sets the hero height itself and centers inside it.
    Column(Modifier.fillMaxWidth().heightIn(min = minHeight), verticalArrangement = Arrangement.Center) {
        Reveal(0) {
            Text(
                "Hi, my name is",
                style = mono(16.sp),
                modifier = Modifier.padding(start = 4.dp, bottom = 30.dp),
            )
        }
        Reveal(1) {
            val shimmer = Brush.linearGradient(
                listOf(PortfolioTheme.colors.textPrimary, PortfolioTheme.colors.textPrimary, PortfolioTheme.colors.accent),
            )
            Text("${header.name}.", style = heading(big).merge(TextStyle(brush = shimmer, fontWeight = FontWeight.Bold)))
        }
        Reveal(2) {
            Text(
                header.shortDesc,
                style = heading(big, PortfolioTheme.colors.textMuted).copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        Reveal(3) {
            Text(
                header.longDesc,
                style = body(18.sp),
                modifier = Modifier.padding(top = 20.dp).widthIn(max = 540.dp),
            )
        }
        Reveal(4, Modifier.padding(top = 50.dp)) {
            OutlineButton("Get In Touch", big = true, onClick = onGetInTouch)
        }
    }
}
