package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
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

private val TabHeight = 42.dp
private val TabWidth = 130.dp

@Composable
fun ExperienceSection(jobs: List<Experience>, desktop: Boolean) {
    var selected by remember(jobs) { mutableIntStateOf(0) }
    Column(Modifier.widthIn(max = 700.dp).fillMaxWidth().padding(vertical = 100.dp)) {
        SectionHeading(2, "Where I've Worked", compact = !desktop)
        if (desktop) {
            Row {
                Box(Modifier.width(IntrinsicSize.Max)) {
                    Column {
                        jobs.forEachIndexed { i, job ->
                            VerticalTab(job.company, i == selected) { selected = i }
                        }
                    }
                    // Track plus one indicator that slides between tabs.
                    Box(Modifier.width(2.dp).height(TabHeight * jobs.size).background(PortfolioTheme.colors.outline))
                    val indicatorY by animateDpAsState(TabHeight * selected, spring(stiffness = Spring.StiffnessMediumLow))
                    Box(Modifier.offset(y = indicatorY).width(2.dp).height(TabHeight).background(PortfolioTheme.colors.accent))
                }
                Spacer(Modifier.width(24.dp))
                JobPanel(jobs[selected], Modifier.weight(1f))
            }
        } else {
            Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                Row {
                    jobs.forEachIndexed { i, job ->
                        HorizontalTab(job.company, i == selected) { selected = i }
                    }
                }
                Box(
                    Modifier.align(Alignment.BottomStart).width(TabWidth * jobs.size).height(2.dp)
                        .background(PortfolioTheme.colors.outline),
                )
                val indicatorX by animateDpAsState(TabWidth * selected, spring(stiffness = Spring.StiffnessMediumLow))
                Box(
                    Modifier.align(Alignment.BottomStart).offset(x = indicatorX).width(TabWidth).height(2.dp)
                        .background(PortfolioTheme.colors.accent),
                )
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
            .height(TabHeight)
            .background(if (hovered || selected) PortfolioTheme.colors.surface else Color.Transparent)
            .linkClick(source, onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(2.dp))
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
            .width(TabWidth)
            .background(if (hovered || selected) PortfolioTheme.colors.surface else Color.Transparent)
            .linkClick(source, onClick),
    ) {
        Box(Modifier.fillMaxWidth().height(TabHeight), contentAlignment = Alignment.Center) {
            Text(label, style = mono(13.sp, if (selected) PortfolioTheme.colors.accent else PortfolioTheme.colors.textMuted), maxLines = 1)
        }
        Spacer(Modifier.height(2.dp))
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
