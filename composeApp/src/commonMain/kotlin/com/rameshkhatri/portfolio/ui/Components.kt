package com.rameshkhatri.portfolio.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.theme.Palette
import com.rameshkhatri.portfolio.theme.heading
import com.rameshkhatri.portfolio.theme.mono
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Hover state for desktop and web; always false on touch screens. */
@Composable
fun rememberHoverSource(): Pair<MutableInteractionSource, Boolean> {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    return source to hovered
}

/** Clickable with a hand cursor and no ripple, like a web link. */
fun Modifier.linkClick(source: MutableInteractionSource, onClick: () -> Unit): Modifier =
    this.hoverable(source)
        .clickable(interactionSource = source, indication = null, onClick = onClick)
        .pointerHoverIcon(PointerIcon.Hand)

/** Lays text out top-to-bottom, like CSS `writing-mode: vertical-rl`. */
fun Modifier.verticalText(): Modifier =
    this.layout { measurable, _ ->
        val p = measurable.measure(Constraints())
        layout(p.height, p.width) {
            p.place(x = -(p.width - p.height) / 2, y = (p.width - p.height) / 2)
        }
    }.rotate(90f)

/** Fade-and-rise entrance, staggered by [index]. Keeps its space while hidden. */
@Composable
fun Reveal(index: Int = 0, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val spec = tween<Float>(durationMillis = 500, delayMillis = 150 + index * 100)
    val alpha by animateFloatAsState(if (shown) 1f else 0f, spec)
    val rise by animateFloatAsState(if (shown) 0f else 1f, spec)
    Box(
        modifier.graphicsLayer {
            this.alpha = alpha
            translationY = rise * 20.dp.toPx()
        },
    ) { content() }
}

@Composable
fun Bullet(topPadding: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier.padding(top = topPadding).size(width = 7.dp, height = 9.dp)) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, size.height / 2)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, Palette.Green)
    }
}

@Composable
fun BulletText(text: String, style: TextStyle, modifier: Modifier = Modifier) {
    val line = style.lineHeight.value.takeIf { !it.isNaN() } ?: (style.fontSize.value * 1.4f)
    Row(modifier) {
        Bullet(topPadding = ((line - 9f) / 2f).coerceAtLeast(0f).dp)
        Spacer(Modifier.width(14.dp))
        Text(text, style = style)
    }
}

@Composable
fun LinkText(
    text: String,
    url: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    hoverColor: Color = Palette.Green,
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

@Composable
fun OutlineButton(text: String, big: Boolean = false, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    val shape = RoundedCornerShape(4.dp)
    Box(
        Modifier
            .clip(shape)
            .border(1.dp, Palette.Green, shape)
            .background(if (hovered) Palette.Green.copy(alpha = 0.1f) else Color.Transparent)
            .linkClick(source, onClick)
            .padding(horizontal = if (big) 28.dp else 16.dp, vertical = if (big) 18.dp else 10.dp),
    ) {
        Text(text, style = mono(if (big) 14.sp else 13.sp))
    }
}

@Composable
fun SectionHeading(number: Int, title: String, compact: Boolean) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("0$number.", style = mono(if (compact) 16.sp else 20.sp))
        Spacer(Modifier.width(10.dp))
        Text(title, style = heading(if (compact) 24.sp else 32.sp))
        Spacer(Modifier.width(20.dp))
        Box(
            Modifier
                .weight(1f)
                .widthIn(max = 300.dp)
                .height(1.dp)
                .background(Palette.LightestNavy),
        )
    }
}

/** Hexagon "R" logo from the original site. */
@Composable
fun Logo(initial: String, modifier: Modifier = Modifier, size: Dp = 42.dp) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size)) {
            val r = this.size.minDimension / 2 - 2.dp.toPx()
            val c = center
            val path = Path()
            for (i in 0 until 6) {
                val a = (60.0 * i - 90.0) * PI / 180.0
                val p = Offset(c.x + r * cos(a).toFloat(), c.y + r * sin(a).toFloat())
                if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            path.close()
            drawPath(path, Palette.Green, style = Stroke(width = 2.dp.toPx()))
        }
        Text(initial, style = mono((size.value * 0.42f).sp).copy(fontWeight = FontWeight.Bold))
    }
}

/** Three-line menu icon that turns into an X when [open]. */
@Composable
fun MenuIcon(open: Boolean, modifier: Modifier = Modifier) {
    val t by animateFloatAsState(if (open) 1f else 0f, tween(250))
    Canvas(modifier.size(width = 30.dp, height = 24.dp)) {
        val stroke = 2.dp.toPx()
        val w = size.width
        val h = size.height
        val mid = h / 2
        if (t < 0.5f) {
            val k = 1f - t * 2f
            drawLine(Palette.Green, Offset(0f, mid - (mid - stroke) * k), Offset(w, mid - (mid - stroke) * k), stroke, StrokeCap.Round)
            drawLine(Palette.Green, Offset(w * 0.2f, mid), Offset(w, mid), stroke, StrokeCap.Round)
            drawLine(Palette.Green, Offset(w * 0.4f, mid + (mid - stroke) * k), Offset(w, mid + (mid - stroke) * k), stroke, StrokeCap.Round)
        } else {
            val k = (t - 0.5f) * 2f
            val dy = (w / 2) * k * 0.8f
            drawLine(Palette.Green, Offset(w * 0.1f, mid - dy), Offset(w * 0.9f, mid + dy), stroke, StrokeCap.Round)
            drawLine(Palette.Green, Offset(w * 0.1f, mid + dy), Offset(w * 0.9f, mid - dy), stroke, StrokeCap.Round)
        }
    }
}

/** Folder outline used on the small project cards. */
@Composable
fun FolderIcon(modifier: Modifier = Modifier) {
    Canvas(modifier.size(width = 38.dp, height = 30.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, h * 0.12f)
            lineTo(w * 0.36f, h * 0.12f)
            lineTo(w * 0.46f, h * 0.28f)
            lineTo(w, h * 0.28f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(path, Palette.Green, style = Stroke(width = 1.5.dp.toPx()))
    }
}

@Composable
fun animateLift(hovered: Boolean, amount: Dp = 5.dp): Dp =
    animateDpAsState(if (hovered) -amount else 0.dp, tween(200)).value
