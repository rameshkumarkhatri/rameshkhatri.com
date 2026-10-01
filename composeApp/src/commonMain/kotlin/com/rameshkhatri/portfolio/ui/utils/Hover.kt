package com.rameshkhatri.portfolio.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

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
