package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.LinkText

@Composable
fun Footer(portfolio: Portfolio, showSocials: Boolean) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (showSocials) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                portfolio.socials.forEach { LinkText(it.label, it.url, mono(12.sp, PortfolioTheme.colors.textSecondary)) }
            }
        }
        val credit = "Designed & Built by ${portfolio.header.name}"
        val creditUrl = portfolio.socials.firstOrNull()?.url
        if (creditUrl != null) {
            LinkText(credit, creditUrl, mono(12.sp, PortfolioTheme.colors.textMuted))
        } else {
            Text(credit, style = mono(12.sp, PortfolioTheme.colors.textMuted))
        }
        Text("Rebuilt with Compose Multiplatform", style = mono(11.sp, PortfolioTheme.colors.textMuted.copy(alpha = 0.7f)))
    }
}
