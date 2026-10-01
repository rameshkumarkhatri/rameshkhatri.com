package com.rameshkhatri.portfolio.ui.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun animateLift(hovered: Boolean, amount: Dp = 5.dp): Dp =
    animateDpAsState(if (hovered) -amount else 0.dp, tween(200)).value
