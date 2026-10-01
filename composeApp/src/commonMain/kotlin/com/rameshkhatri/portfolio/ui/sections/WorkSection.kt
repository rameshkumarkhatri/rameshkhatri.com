package com.rameshkhatri.portfolio.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Project
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.body
import com.rameshkhatri.portfolio.designsystem.heading
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.components.FolderIcon
import com.rameshkhatri.portfolio.ui.components.LinkText
import com.rameshkhatri.portfolio.ui.components.SectionHeading
import com.rameshkhatri.portfolio.ui.utils.animateLift
import com.rameshkhatri.portfolio.ui.utils.linkClick
import com.rameshkhatri.portfolio.ui.utils.rememberHoverSource

@Composable
fun WorkSection(featured: List<Project>, caseStudies: List<Project>, desktop: Boolean) {
    Column(Modifier.widthIn(max = 1000.dp).fillMaxWidth().padding(vertical = 100.dp)) {
        SectionHeading(3, "Some Things I've Built", compact = !desktop)
        featured.forEachIndexed { i, p ->
            FeaturedCard(p, alignEnd = desktop && i % 2 == 1, desktop = desktop)
            Spacer(Modifier.height(if (desktop) 90.dp else 50.dp))
        }
        if (caseStudies.isNotEmpty()) {
            Text(
                "Case Studies",
                style = heading(if (desktop) 30.sp else 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 40.dp),
            )
            ProjectGrid(caseStudies)
        }
    }
}

@Composable
private fun FeaturedCard(p: Project, alignEnd: Boolean, desktop: Boolean) {
    val align = if (alignEnd) Alignment.End else Alignment.Start
    val textAlign = if (alignEnd) TextAlign.End else TextAlign.Start
    val uri = LocalUriHandler.current
    Column(Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Text("Featured Project", style = mono(13.sp))
        val (source, hovered) = rememberHoverSource()
        val link = p.linkUrl?.takeIf { it.isNotBlank() }
        Text(
            p.name,
            style = heading(28.sp, if (hovered && link != null) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary),
            textAlign = textAlign,
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                .then(if (link != null) Modifier.linkClick(source) { uri.openUri(link) } else Modifier),
        )
        Box(
            Modifier
                .then(if (desktop) Modifier.widthIn(max = 600.dp) else Modifier.fillMaxWidth())
                .shadow(12.dp, RoundedCornerShape(4.dp))
                .background(PortfolioTheme.colors.surface, RoundedCornerShape(4.dp))
                .padding(25.dp),
        ) {
            Text(p.details, style = body(17.sp, PortfolioTheme.colors.textSecondary), textAlign = textAlign)
        }
        Text(
            p.stack.joinToString("    "),
            style = mono(13.sp, PortfolioTheme.colors.textSecondary),
            textAlign = textAlign,
            modifier = Modifier.padding(top = 25.dp),
        )
        if (link != null) {
            LinkText("View Project", link, mono(13.sp, PortfolioTheme.colors.textPrimary), Modifier.padding(top = 10.dp))
        }
    }
}

@Composable
private fun ProjectGrid(projects: List<Project>) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = when {
            maxWidth >= 900.dp -> 3
            maxWidth >= 580.dp -> 2
            else -> 1
        }
        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            projects.chunked(columns).forEach { row ->
                Row(
                    Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    row.forEach { ProjectCard(it, Modifier.weight(1f).fillMaxHeight()) }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(p: Project, modifier: Modifier) {
    val (source, hovered) = rememberHoverSource()
    val lift = animateLift(hovered, 7.dp)
    val uri = LocalUriHandler.current
    val link = p.linkUrl?.takeIf { it.isNotBlank() }
    Column(
        modifier
            .graphicsLayer { translationY = lift.toPx() }
            .shadow(if (hovered) 16.dp else 6.dp, RoundedCornerShape(4.dp))
            .background(PortfolioTheme.colors.surface, RoundedCornerShape(4.dp))
            .linkClick(source) { link?.let { uri.openUri(it) } }
            .padding(horizontal = 28.dp, vertical = 30.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FolderIcon()
            Spacer(Modifier.weight(1f))
            if (link != null) LinkText("View", link, mono(12.sp, PortfolioTheme.colors.textSecondary))
        }
        Text(
            p.name,
            style = heading(20.sp, if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary),
            modifier = Modifier.padding(top = 28.dp, bottom = 10.dp),
        )
        Text(p.details, style = body(15.sp, PortfolioTheme.colors.textSecondary))
        Spacer(Modifier.weight(1f).heightIn(min = 20.dp))
        Text(p.stack.joinToString("   "), style = mono(12.sp, PortfolioTheme.colors.textMuted))
    }
}
