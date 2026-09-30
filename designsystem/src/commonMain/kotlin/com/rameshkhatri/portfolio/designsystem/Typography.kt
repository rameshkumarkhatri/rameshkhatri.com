package com.rameshkhatri.portfolio.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.designsystem.resources.Res
import com.rameshkhatri.portfolio.designsystem.resources.fira_code_bold
import com.rameshkhatri.portfolio.designsystem.resources.fira_code_regular
import org.jetbrains.compose.resources.Font

/** Bundled Fira Code, since the web target has no system monospace font. Provided by [PortfolioTheme]. */
internal val LocalMonoFamily = staticCompositionLocalOf<FontFamily> { FontFamily.Monospace }

@Composable
internal fun rememberMonoFamily(): FontFamily = FontFamily(
    Font(Res.font.fira_code_regular, FontWeight.Normal),
    Font(Res.font.fira_code_bold, FontWeight.Bold),
)

/** Monospace label style. Defaults to the accent color. */
@Composable
@ReadOnlyComposable
fun mono(size: TextUnit, color: Color = PortfolioTheme.colors.accent): TextStyle =
    TextStyle(fontFamily = LocalMonoFamily.current, fontSize = size, color = color, lineHeight = size * 1.4f)

/** Body copy. Defaults to the muted text color. */
@Composable
@ReadOnlyComposable
fun body(size: TextUnit = 17.sp, color: Color = PortfolioTheme.colors.textMuted): TextStyle =
    TextStyle(fontSize = size, color = color, lineHeight = size * 1.5f)

/** Headings. Defaults to the primary text color. */
@Composable
@ReadOnlyComposable
fun heading(size: TextUnit, color: Color = PortfolioTheme.colors.textPrimary): TextStyle =
    TextStyle(fontSize = size, color = color, fontWeight = FontWeight.SemiBold, lineHeight = size * 1.15f)
