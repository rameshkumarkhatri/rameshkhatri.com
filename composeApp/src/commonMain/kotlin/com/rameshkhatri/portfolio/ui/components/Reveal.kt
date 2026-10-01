package com.rameshkhatri.portfolio.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

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
