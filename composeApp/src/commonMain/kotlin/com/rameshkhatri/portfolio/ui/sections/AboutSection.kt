package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.About
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.body
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.BulletText
import com.rameshkhatri.portfolio.ui.components.SectionHeading
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun AboutSection(about: About, initials: String, desktop: Boolean) {
    Column(Modifier.widthIn(max = 900.dp).fillMaxWidth().padding(vertical = 100.dp)) {
        SectionHeading(1, about.title, compact = !desktop)
        if (desktop) {
            Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                AboutText(about, Modifier.weight(3f))
            }
        } else {
            AboutText(about, Modifier.fillMaxWidth())
            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
private fun AboutText(about: About, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        about.details.forEach { Text(it, style = body()) }
        if (about.techStack.isEmpty()) return@Column
        Text("Here are a few technologies I've been working with recently:", style = body())
        Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            about.techStack.chunked(2).forEach { pair ->
                Row {
                    pair.forEach {
                        BulletText(
                            it,
                            mono(13.sp, PortfolioTheme.colors.textMuted),
                            Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}


