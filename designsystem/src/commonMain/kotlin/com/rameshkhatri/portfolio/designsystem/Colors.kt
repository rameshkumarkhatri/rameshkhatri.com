package com.rameshkhatri.portfolio.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic color slots used by every screen. Read the current set through [PortfolioTheme.colors];
 * never hardcode a raw palette value in UI code so both modes stay in sync.
 */
@Immutable
data class PortfolioColors(
    /** Page background. */
    val background: Color,
    /** Cards, nav drawer, selected tabs. */
    val surface: Color,
    /** Hairlines, dividers, inactive tab indicators. */
    val outline: Color,
    /** Headings and emphasized text. */
    val textPrimary: Color,
    /** Secondary labels and card body copy. */
    val textSecondary: Color,
    /** Body copy and de-emphasized labels. */
    val textMuted: Color,
    /** Brand highlight: links, numbers, button outlines, icons. */
    val accent: Color,
    /** Overlay behind the mobile menu. */
    val scrim: Color,
    val isDark: Boolean,
)

/** Raw brand tokens. Prefer the semantic slots in [PortfolioColors]. */
object PortfolioPalette {
    // Dark (original Gatsby site)
    val Navy = Color(0xFF0A192F)
    val LightNavy = Color(0xFF112240)
    val LightestNavy = Color(0xFF233554)
    val Slate = Color(0xFF8892B0)
    val LightSlate = Color(0xFFA8B2D1)
    val LightestSlate = Color(0xFFCCD6F6)
    val White = Color(0xFFE6F1FF)
    val Green = Color(0xFF64FFDA)

    // Light
    val Snow = Color(0xFFF7F9FC)
    val Paper = Color(0xFFFFFFFF)
    val Mist = Color(0xFFD9E0EC)
    val Ink = Color(0xFF0A192F)
    val Graphite = Color(0xFF3D4B6B)
    val Steel = Color(0xFF5A6785)
    val Teal = Color(0xFF0B7A68)
}

val DarkPortfolioColors = PortfolioColors(
    background = PortfolioPalette.Navy,
    surface = PortfolioPalette.LightNavy,
    outline = PortfolioPalette.LightestNavy,
    textPrimary = PortfolioPalette.LightestSlate,
    textSecondary = PortfolioPalette.LightSlate,
    textMuted = PortfolioPalette.Slate,
    accent = PortfolioPalette.Green,
    scrim = Color.Black.copy(alpha = 0.5f),
    isDark = true,
)

val LightPortfolioColors = PortfolioColors(
    background = PortfolioPalette.Snow,
    surface = PortfolioPalette.Paper,
    outline = PortfolioPalette.Mist,
    textPrimary = PortfolioPalette.Ink,
    textSecondary = PortfolioPalette.Graphite,
    textMuted = PortfolioPalette.Steel,
    accent = PortfolioPalette.Teal,
    scrim = PortfolioPalette.Ink.copy(alpha = 0.35f),
    isDark = false,
)

internal val LocalPortfolioColors = staticCompositionLocalOf { DarkPortfolioColors }
