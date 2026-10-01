package com.rameshkhatri.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.utils.verticalText

@Composable
fun SocialRail(portfolio: Portfolio, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        portfolio.socials.forEach { link ->
            LinkText(
                link.label,
                link.url,
                mono(12.sp, PortfolioTheme.colors.textSecondary).copy(letterSpacing = 0.1.em),
                Modifier.verticalText(),
            )
            Spacer(Modifier.height(24.dp))
        }
        Box(Modifier.width(1.dp).height(90.dp).background(PortfolioTheme.colors.textSecondary))
    }
}

@Composable
fun EmailRail(email: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LinkText(
            email,
            "mailto:$email",
            mono(12.sp, PortfolioTheme.colors.textSecondary).copy(letterSpacing = 0.1.em),
            Modifier.verticalText(),
        )
        Spacer(Modifier.height(24.dp))
        Box(Modifier.width(1.dp).height(90.dp).background(PortfolioTheme.colors.textSecondary))
    }
}
