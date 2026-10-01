package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.heading
import com.rameshkhatri.portfolio.designsystem.mono

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
                .background(PortfolioTheme.colors.outline),
        )
    }
}
