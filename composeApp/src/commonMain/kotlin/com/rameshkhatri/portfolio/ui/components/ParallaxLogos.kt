package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.resources.Res
import com.rameshkhatri.portfolio.resources.logo_android
import com.rameshkhatri.portfolio.resources.logo_apple
import com.rameshkhatri.portfolio.resources.logo_flutter
import com.rameshkhatri.portfolio.resources.logo_kotlin
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * One floating platform logo. [depth] 0 = far (small, faint, barely moves), 1 = near.
 * [x] / [y] are fractions of the container.
 */
private class FloatingLogo(
    val res: DrawableResource,
    val x: Float,
    val y: Float,
    val size: Dp,
    val depth: Float,
    val tilt: Float,
)

private val DesktopLogos = listOf(
    FloatingLogo(Res.drawable.logo_android, x = 0.62f, y = 0.16f, size = 120.dp, depth = 0.55f, tilt = -8f),
    FloatingLogo(Res.drawable.logo_apple, x = 0.86f, y = 0.30f, size = 150.dp, depth = 0.85f, tilt = 10f),
    FloatingLogo(Res.drawable.logo_flutter, x = 0.70f, y = 0.60f, size = 100.dp, depth = 0.35f, tilt = 6f),
    FloatingLogo(Res.drawable.logo_kotlin, x = 0.90f, y = 0.72f, size = 80.dp, depth = 1.0f, tilt = -12f),
)

private val MobileLogos = listOf(
    FloatingLogo(Res.drawable.logo_android, x = 0.10f, y = 0.04f, size = 64.dp, depth = 0.5f, tilt = -8f),
    FloatingLogo(Res.drawable.logo_apple, x = 0.66f, y = 0.10f, size = 84.dp, depth = 0.9f, tilt = 10f),
    FloatingLogo(Res.drawable.logo_flutter, x = 0.08f, y = 0.80f, size = 64.dp, depth = 0.35f, tilt = 6f),
    FloatingLogo(Res.drawable.logo_kotlin, x = 0.70f, y = 0.82f, size = 56.dp, depth = 1.0f, tilt = -12f),
)

/**
 * Android, iOS, Flutter and Kotlin marks floating behind the hero. Each layer scrolls at its
 * own speed and leans toward the pointer, so the group reads as having depth. [scroll] and
 * [pointer] are lambdas so the layers update in the draw phase without recomposing.
 */
@Composable
fun ParallaxLogos(
    scroll: () -> Int,
    pointer: () -> Offset?,
    desktop: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent = PortfolioTheme.colors.accent
    // Fainter on phones, where the marks sit closer to the copy.
    val baseAlpha = (if (PortfolioTheme.colors.isDark) 0.22f else 0.16f) * (if (desktop) 1f else 0.7f)
    val logos = if (desktop) DesktopLogos else MobileLogos
    BoxWithConstraints(modifier) {
        val w = maxWidth
        val h = maxHeight
        logos.forEach { logo ->
            Image(
                painter = painterResource(logo.res),
                contentDescription = null,
                colorFilter = ColorFilter.tint(accent),
                modifier = Modifier
                    .offset(x = w * logo.x, y = h * logo.y)
                    .size(logo.size)
                    .graphicsLayer {
                        // Deeper layers lag the page more and follow the pointer more.
                        val s = scroll().toFloat()
                        val p = pointer()
                        val px = if (p != null) (p.x - size.width / 2f) * 0.035f * logo.depth else 0f
                        val py = if (p != null) (p.y - size.height / 2f) * 0.035f * logo.depth else 0f
                        translationY = s * 0.45f * logo.depth + py
                        translationX = px
                        rotationZ = logo.tilt + s * 0.015f * logo.depth
                        alpha = baseAlpha * (0.55f + 0.45f * logo.depth)
                    },
            )
        }
    }
}
