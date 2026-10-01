package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.designsystem.body
import com.rameshkhatri.portfolio.designsystem.heading
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.OutlineButton

@Composable
fun ContactSection(portfolio: Portfolio, desktop: Boolean) {
    val uri = LocalUriHandler.current
    Column(
        Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("04. What's Next?", style = mono(16.sp))
        Text(
            portfolio.contact.title,
            style = heading(if (desktop) 56.sp else 40.sp).copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
        )
        Text(portfolio.contact.message, style = body(18.sp), textAlign = TextAlign.Center)
        if (portfolio.email.isNotBlank()) {
            Spacer(Modifier.height(50.dp))
            OutlineButton("Say Hello", big = true) { uri.openUri("mailto:${portfolio.email}") }
        }
    }
}
