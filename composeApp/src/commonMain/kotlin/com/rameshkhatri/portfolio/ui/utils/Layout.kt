package com.rameshkhatri.portfolio.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints

/** Lays text out top-to-bottom, like CSS `writing-mode: vertical-rl`. */
fun Modifier.verticalText(): Modifier =
    this.layout { measurable, _ ->
        val p = measurable.measure(Constraints())
        layout(p.height, p.width) {
            p.place(x = -(p.width - p.height) / 2, y = (p.width - p.height) / 2)
        }
    }.rotate(90f)
