package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.Section
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.ThemeState
import com.rameshkhatri.portfolio.designsystem.isDark
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun MobileMenu(
    open: Boolean,
    onClose: () -> Unit,
    showResume: Boolean,
    isDark: Boolean,
    onNavigate: (Section) -> Unit,
    onResume: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(open, enter = fadeIn(), exit = fadeOut()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(PortfolioTheme.colors.scrim)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClose),
            )
        }
        AnimatedVisibility(
            open,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().fillMaxWidth(0.75f),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shadow(20.dp)
                    .background(PortfolioTheme.colors.surface)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            ) {
                val (closeSource, _) = rememberHoverSource()
                MenuIcon(
                    open = true,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 28.dp, end = 25.dp)
                        .linkClick(closeSource, onClose),
                )
                Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(36.dp),
                ) {
                    Section.entries.forEachIndexed { i, s ->
                        val (source, hovered) = rememberHoverSource()
                        Column(
                            Modifier.linkClick(source) { onNavigate(s) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("0${i + 1}.", style = mono(14.sp))
                            Text(
                                s.label,
                                style = mono(18.sp, if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary),
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("0${Section.entries.size + 1}.", style = mono(14.sp))
                        Text("Theme", style = mono(18.sp, PortfolioTheme.colors.textPrimary), modifier = Modifier.padding(top = 4.dp))
                        val selectedId = ThemeState.selected?.id
                        Row(Modifier.padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ThemeState.themes.forEach { spec ->
                                val (source, _) = rememberHoverSource()
                                ThemeSwatch(
                                    spec,
                                    isDark,
                                    selected = spec.id == selectedId,
                                    modifier = Modifier.linkClick(source) { ThemeState.select(spec.id) },
                                )
                            }
                        }
                    }
                    if (showResume) OutlineButton("Resume", big = true, onClick = onResume)
                }
            }
        }
    }
}
