package com.rameshkhatri.portfolio.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.resources.Res
import com.rameshkhatri.portfolio.resources.fira_code_bold
import com.rameshkhatri.portfolio.resources.fira_code_regular
import org.jetbrains.compose.resources.Font

/** Colors from the original Gatsby site. */
object Palette {
    val Navy = Color(0xFF0A192F)
    val LightNavy = Color(0xFF112240)
    val LightestNavy = Color(0xFF233554)
    val Slate = Color(0xFF8892B0)
    val LightSlate = Color(0xFFA8B2D1)
    val LightestSlate = Color(0xFFCCD6F6)
    val White = Color(0xFFE6F1FF)
    val Green = Color(0xFF64FFDA)
}

/** Bundled Fira Code, since the web target has no system monospace font. Provided by [PortfolioTheme]. */
private val LocalMono = staticCompositionLocalOf<FontFamily> { FontFamily.Monospace }

@Composable
fun mono(size: TextUnit, color: Color = Palette.Green) =
    TextStyle(fontFamily = LocalMono.current, fontSize = size, color = color, lineHeight = size * 1.4f)

fun body(size: TextUnit = 17.sp, color: Color = Palette.Slate) =
    TextStyle(fontSize = size, color = color, lineHeight = size * 1.5f)

fun heading(size: TextUnit, color: Color = Palette.LightestSlate) =
    TextStyle(fontSize = size, color = color, fontWeight = FontWeight.SemiBold, lineHeight = size * 1.15f)

@Composable
fun PortfolioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Palette.Green,
            background = Palette.Navy,
            surface = Palette.LightNavy,
            onBackground = Palette.Slate,
            onSurface = Palette.LightSlate,
        ),
    ) {
        val monoFamily = FontFamily(
            Font(Res.font.fira_code_regular, FontWeight.Normal),
            Font(Res.font.fira_code_bold, FontWeight.Bold),
        )
        CompositionLocalProvider(
            LocalContentColor provides Palette.Slate,
            LocalMono provides monoFamily,
            content = content,
        )
    }
}
