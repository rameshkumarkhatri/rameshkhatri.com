package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Experience
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.body
import com.rameshkhatri.portfolio.designsystem.heading
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.BulletText
import com.rameshkhatri.portfolio.ui.components.SectionHeading
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun ExperienceSection(jobs: List<Experience>, desktop: Boolean) {
    var selected by remember(jobs) { mutableIntStateOf(0) }
    Column(Modifier.widthIn(max = 700.dp).fillMaxWidth().padding(vertical = 100.dp)) {
        SectionHeading(2, "Where I've Worked", compact = !desktop)
        if (desktop) {
            Row {
                Column(Modifier.width(IntrinsicSize.Max)) {
                    jobs.forEachIndexed { i, job ->
                        VerticalTab(job.company, i == selected) { selected = i }
                    }
                }
                Spacer(Modifier.width(24.dp))
                JobPanel(jobs[selected], Modifier.weight(1f))
            }
        } else {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                jobs.forEachIndexed { i, job ->
                    HorizontalTab(job.company, i == selected) { selected = i }
                }
            }
            Spacer(Modifier.height(24.dp))
            JobPanel(jobs[selected], Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun VerticalTab(label: String, selected: Boolean, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    Row(
        Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(if (hovered || selected) PortfolioTheme.colors.surface else Color.Transparent)
            .linkClick(source, onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.width(2.dp).fillMaxHeight()
                .background(if (selected) PortfolioTheme.colors.accent else PortfolioTheme.colors.outline),
        )
        Text(
            label,
            style = mono(13.sp, if (selected || hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textMuted),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
    }
}

@Composable
private fun HorizontalTab(label: String, selected: Boolean, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    Column(
        Modifier
            .width(130.dp)
            .background(if (hovered || selected) PortfolioTheme.colors.surface else Color.Transparent)
            .linkClick(source, onClick),
    ) {
        Box(Modifier.fillMaxWidth().height(42.dp), contentAlignment = Alignment.Center) {
            Text(label, style = mono(13.sp, if (selected) PortfolioTheme.colors.accent else PortfolioTheme.colors.textMuted), maxLines = 1)
        }
        Box(
            Modifier.fillMaxWidth().height(2.dp)
                .background(if (selected) PortfolioTheme.colors.accent else PortfolioTheme.colors.outline),
        )
    }
}

@Composable
private fun JobPanel(job: Experience, modifier: Modifier) {
    val uri = LocalUriHandler.current
    AnimatedContent(
        targetState = job,
        transitionSpec = { fadeIn(tween(250, delayMillis = 100)) togetherWith fadeOut(tween(100)) },
        modifier = modifier,
    ) { j ->
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val (source, _) = rememberHoverSource()
            val url = j.url
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = PortfolioTheme.colors.textPrimary)) { append(j.title) }
                    withStyle(SpanStyle(color = PortfolioTheme.colors.accent)) { append(" @ ${j.company}") }
                },
                style = heading(22.sp).copy(fontWeight = FontWeight.Medium),
                modifier = if (url != null) Modifier.linkClick(source) { uri.openUri(url) } else Modifier,
            )
            Text(
                listOf(j.range, j.location).filter { it.isNotBlank() }.joinToString("  ·  "),
                style = mono(13.sp, PortfolioTheme.colors.textSecondary),
            )
            Spacer(Modifier.height(16.dp))
            j.bullets.forEach { BulletText(it, body(16.sp)) }
        }
    }
}
