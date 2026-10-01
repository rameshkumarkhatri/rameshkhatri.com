package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.Section
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.ThemeSpec
import com.rameshkhatri.portfolio.designsystem.ThemeState
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

val NavHeight = 80.dp

val NavHeightScrolled = 70.dp

@Composable
fun NavBar(
    desktop: Boolean,
    scrolled: Boolean,
    menuOpen: Boolean,
    initial: String,
    showResume: Boolean,
    onLogo: () -> Unit,
    onNavigate: (Section) -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val height by animateDpAsState(if (scrolled) NavHeightScrolled else NavHeight)
    Row(
        Modifier
            .fillMaxWidth()
            .height(height)
            .then(if (scrolled) Modifier.shadow(10.dp) else Modifier)
            .background(PortfolioTheme.colors.background.copy(alpha = 0.95f))
            .padding(horizontal = if (desktop) 50.dp else 25.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (logoSource, _) = rememberHoverSource()
        Reveal(0) { Logo(initial, Modifier.linkClick(logoSource, onLogo)) }
        Spacer(Modifier.weight(1f))
        if (desktop) {
            Section.entries.forEachIndexed { i, s ->
                Reveal(i + 1) { NavLink(i + 1, s.label) { onNavigate(s) } }
            }
            Reveal(Section.entries.size + 1) { ThemeMenu(Section.entries.size + 1, isDark) }
            Reveal(Section.entries.size + 2) { ThemeToggle(isDark, onToggleTheme) }
            Spacer(Modifier.width(15.dp))
            if (showResume) {
                Reveal(Section.entries.size + 3) { OutlineButton("Resume", onClick = onResume) }
            }
        } else {
            ThemeToggle(isDark, onToggleTheme)
            Spacer(Modifier.width(8.dp))
            val (menuSource, _) = rememberHoverSource()
            MenuIcon(menuOpen, Modifier.linkClick(menuSource, onMenu))
        }
    }
}

@Composable
private fun NavLink(number: Int, label: String, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    Row(Modifier.linkClick(source, onClick).padding(10.dp)) {
        Text("0$number.", style = mono(13.sp))
        Text(" $label", style = mono(13.sp, if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary))
    }
}

/** "05. Theme" nav tab with a dropdown of the themes from themes.json. */
@Composable
private fun ThemeMenu(number: Int, isDark: Boolean) {
    var open by remember { mutableStateOf(false) }
    Box {
        NavLink(number, "Theme") { open = !open }
        DropdownMenu(
            expanded = open,
            onDismissRequest = { open = false },
            offset = DpOffset(0.dp, 6.dp),
            shape = RoundedCornerShape(4.dp),
            containerColor = PortfolioTheme.colors.surface,
            border = BorderStroke(1.dp, PortfolioTheme.colors.outline),
        ) {
            val selectedId = ThemeState.selected?.id
            ThemeState.themes.forEach { spec ->
                ThemeMenuItem(spec, isDark, selected = spec.id == selectedId) {
                    ThemeState.select(spec.id)
                    open = false
                }
            }
        }
    }
}

@Composable
private fun ThemeMenuItem(spec: ThemeSpec, isDark: Boolean, selected: Boolean, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    val color = when {
        selected || hovered -> PortfolioTheme.colors.accent
        else -> PortfolioTheme.colors.textPrimary
    }
    Row(
        Modifier
            .fillMaxWidth()
            .linkClick(source, onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ThemeSwatch(spec, isDark, selected, size = 20.dp)
        Text(spec.name, style = mono(13.sp, color), modifier = Modifier.padding(end = 8.dp))
    }
}
