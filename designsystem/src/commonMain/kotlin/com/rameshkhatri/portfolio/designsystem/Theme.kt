package com.rameshkhatri.portfolio.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** How the app picks between the dark and light palettes. */
enum class ThemeMode { System, Light, Dark }

@Composable
fun ThemeMode.isDark(): Boolean = when (this) {
    ThemeMode.System -> isSystemInDarkTheme()
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
}

/**
 * App-wide theme selection: which named theme from themes.json is active, and whether it is
 * shown in dark or light mode. Plain Kotlin state (no Compose types in its API) so any platform
 * entry point, including Swift on iOS, can read or set it; Compose recomposes on change.
 */
object ThemeState {
    var mode: ThemeMode by mutableStateOf(ThemeMode.System)

    /** Themes from themes.json. Empty until [load] completes. */
    var themes: List<ThemeSpec> by mutableStateOf(emptyList())
        private set

    var selectedId: String? by mutableStateOf(null)

    val selected: ThemeSpec?
        get() = themes.firstOrNull { it.id == selectedId } ?: themes.firstOrNull()

    /** Reads themes.json once and picks its default theme if nothing is selected yet. */
    suspend fun load() {
        if (themes.isNotEmpty()) return
        val catalog = loadThemeCatalog()
        themes = catalog.themes
        if (selectedId == null) selectedId = catalog.default ?: catalog.themes.firstOrNull()?.id
    }

    fun select(id: String) {
        selectedId = id
    }

    /** Flips to the opposite of what is currently shown, given the resolved [currentlyDark]. */
    fun toggle(currentlyDark: Boolean) {
        mode = if (currentlyDark) ThemeMode.Light else ThemeMode.Dark
    }

    /** Colors for the selected theme; the built-in Midnight palette until the catalog loads. */
    fun colors(isDark: Boolean): PortfolioColors =
        selected?.colors(isDark) ?: if (isDark) DarkPortfolioColors else LightPortfolioColors
}

/**
 * Root theme for the portfolio. Pass [darkTheme] to force a mode, or [colors] to supply a
 * custom [PortfolioColors] set; by default it follows the system setting.
 */
@Composable
fun PortfolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: PortfolioColors = if (darkTheme) DarkPortfolioColors else LightPortfolioColors,
    content: @Composable () -> Unit,
) {
    val scheme = if (colors.isDark) {
        darkColorScheme(
            primary = colors.accent,
            background = colors.background,
            surface = colors.surface,
            onBackground = colors.textMuted,
            onSurface = colors.textSecondary,
            outline = colors.outline,
        )
    } else {
        lightColorScheme(
            primary = colors.accent,
            background = colors.background,
            surface = colors.surface,
            onBackground = colors.textMuted,
            onSurface = colors.textSecondary,
            outline = colors.outline,
        )
    }
    MaterialTheme(colorScheme = scheme) {
        CompositionLocalProvider(
            LocalPortfolioColors provides colors,
            LocalContentColor provides colors.textMuted,
            LocalMonoFamily provides rememberMonoFamily(),
            content = content,
        )
    }
}

/** Accessors for the current theme values, mirroring [MaterialTheme]. */
object PortfolioTheme {
    val colors: PortfolioColors
        @Composable @ReadOnlyComposable get() = LocalPortfolioColors.current
}
