package com.rameshkhatri.portfolio.designsystem

import androidx.compose.ui.graphics.Color
import com.rameshkhatri.portfolio.designsystem.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

/*
 * Themes are defined in composeResources/files/themes.json. Each theme has a dark and a
 * light variant; every variant must supply the semantic slots of [PortfolioColors] as
 * "#RRGGBB" or "#AARRGGBB" hex strings. Add an entry to the file to add a theme.
 */

@Serializable
data class ThemeCatalog(
    /** Id of the theme selected on first launch. Falls back to the first entry. */
    val default: String? = null,
    val themes: List<ThemeSpec> = emptyList(),
)

@Serializable
data class ThemeSpec(
    val id: String,
    val name: String,
    val dark: ColorSpec,
    val light: ColorSpec,
) {
    fun colors(isDark: Boolean): PortfolioColors = if (isDark) dark.toColors(true) else light.toColors(false)
}

@Serializable
data class ColorSpec(
    val background: String,
    val surface: String,
    val outline: String,
    @SerialName("text_primary") val textPrimary: String,
    @SerialName("text_secondary") val textSecondary: String,
    @SerialName("text_muted") val textMuted: String,
    val accent: String,
    /** Optional. Defaults to a translucent black (dark) or translucent primary text (light). */
    val scrim: String? = null,
) {
    fun toColors(isDark: Boolean): PortfolioColors {
        val primary = parseHexColor(textPrimary)
        return PortfolioColors(
            background = parseHexColor(background),
            surface = parseHexColor(surface),
            outline = parseHexColor(outline),
            textPrimary = primary,
            textSecondary = parseHexColor(textSecondary),
            textMuted = parseHexColor(textMuted),
            accent = parseHexColor(accent),
            scrim = scrim?.let(::parseHexColor)
                ?: if (isDark) Color.Black.copy(alpha = 0.5f) else primary.copy(alpha = 0.35f),
            isDark = isDark,
        )
    }
}

/** Parses "#RRGGBB" or "#AARRGGBB" (leading '#' optional). */
fun parseHexColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    require(clean.length == 6 || clean.length == 8) { "Bad color '$hex': expected #RRGGBB or #AARRGGBB" }
    val value = clean.toLong(16)
    return if (clean.length == 6) Color(0xFF000000L or value) else Color(value)
}

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalResourceApi::class)
suspend fun loadThemeCatalog(): ThemeCatalog =
    json.decodeFromString(ThemeCatalog.serializer(), Res.readBytes("files/themes.json").decodeToString())
