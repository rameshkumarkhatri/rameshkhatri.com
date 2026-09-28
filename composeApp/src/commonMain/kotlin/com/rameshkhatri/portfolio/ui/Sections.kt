package com.rameshkhatri.portfolio.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.About
import com.rameshkhatri.portfolio.data.Experience
import com.rameshkhatri.portfolio.data.Header
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.data.Project
import com.rameshkhatri.portfolio.theme.Palette
import com.rameshkhatri.portfolio.theme.body
import com.rameshkhatri.portfolio.theme.heading
import com.rameshkhatri.portfolio.theme.mono

// ---------------------------------------------------------------- Hero

@Composable
fun HeroSection(header: Header, contentWidth: Dp, minHeight: Dp, topPadding: Dp, onGetInTouch: () -> Unit) {
    val big = (contentWidth.value * 0.08f).coerceIn(40f, 80f).sp
    // Fills the first screen; [topPadding] keeps the content clear of the overlaid nav bar.
    Column(
        Modifier.widthIn(max = 1000.dp).fillMaxWidth().heightIn(min = minHeight).padding(top = topPadding),
        verticalArrangement = Arrangement.Center,
    ) {
        Reveal(0) {
            Text(
                "Hi, my name is",
                style = mono(16.sp),
                modifier = Modifier.padding(start = 4.dp, bottom = 30.dp),
            )
        }
        Reveal(1) { Text("${header.name}.", style = heading(big).copy(fontWeight = FontWeight.Bold)) }
        Reveal(2) {
            Text(
                header.shortDesc,
                style = heading(big, Palette.Slate).copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        Reveal(3) {
            Text(
                header.longDesc,
                style = body(18.sp),
                modifier = Modifier.padding(top = 20.dp).widthIn(max = 540.dp),
            )
        }
        Reveal(4, Modifier.padding(top = 50.dp)) {
            OutlineButton("Get In Touch", big = true, onClick = onGetInTouch)
        }
    }
}

// ---------------------------------------------------------------- About

@Composable
fun AboutSection(about: About, initials: String, desktop: Boolean) {
    Column(Modifier.widthIn(max = 900.dp).fillMaxWidth().padding(vertical = 100.dp)) {
        SectionHeading(1, about.title, compact = !desktop)
        if (desktop) {
            Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                AboutText(about, Modifier.weight(3f))
                Portrait(initials, Modifier.weight(2f))
            }
        } else {
            AboutText(about, Modifier.fillMaxWidth())
            Spacer(Modifier.height(50.dp))
            Portrait(initials, Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.7f))
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
                    pair.forEach { BulletText(it, mono(13.sp, Palette.Slate), Modifier.weight(1f)) }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

/** Placeholder for the headshot. Swap the initials for an Image once you add a photo resource. */
@Composable
private fun Portrait(initials: String, modifier: Modifier) {
    val (source, hovered) = rememberHoverSource()
    val frame = if (hovered) 14.dp else 20.dp
    val shape = RoundedCornerShape(4.dp)
    Box(modifier.widthIn(max = 300.dp).aspectRatio(1f).padding(end = 20.dp, bottom = 20.dp)) {
        Box(
            Modifier.fillMaxSize().offset(frame, frame).border(2.dp, Palette.Green, shape),
        )
        Box(
            Modifier
                .fillMaxSize()
                .offset(if (hovered) (-4).dp else 0.dp, if (hovered) (-4).dp else 0.dp)
                .clip(shape)
                .background(Palette.LightNavy)
                .background(if (hovered) Color.Transparent else Palette.Green.copy(alpha = 0.12f))
                .hoverable(source),
            contentAlignment = Alignment.Center,
        ) {
            Text(initials, style = mono(56.sp).copy(fontWeight = FontWeight.Bold))
        }
    }
}

// ---------------------------------------------------------------- Experience

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
            .background(if (hovered || selected) Palette.LightNavy else Color.Transparent)
            .linkClick(source, onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.width(2.dp).fillMaxHeight()
                .background(if (selected) Palette.Green else Palette.LightestNavy),
        )
        Text(
            label,
            style = mono(13.sp, if (selected || hovered) Palette.Green else Palette.Slate),
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
            .background(if (hovered || selected) Palette.LightNavy else Color.Transparent)
            .linkClick(source, onClick),
    ) {
        Box(Modifier.fillMaxWidth().height(42.dp), contentAlignment = Alignment.Center) {
            Text(label, style = mono(13.sp, if (selected) Palette.Green else Palette.Slate), maxLines = 1)
        }
        Box(
            Modifier.fillMaxWidth().height(2.dp)
                .background(if (selected) Palette.Green else Palette.LightestNavy),
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
                    withStyle(SpanStyle(color = Palette.LightestSlate)) { append(j.title) }
                    withStyle(SpanStyle(color = Palette.Green)) { append(" @ ${j.company}") }
                },
                style = heading(22.sp).copy(fontWeight = FontWeight.Medium),
                modifier = if (url != null) Modifier.linkClick(source) { uri.openUri(url) } else Modifier,
            )
            Text(
                listOf(j.range, j.location).filter { it.isNotBlank() }.joinToString("  ·  "),
                style = mono(13.sp, Palette.LightSlate),
            )
            Spacer(Modifier.height(16.dp))
            j.bullets.forEach { BulletText(it, body(16.sp)) }
        }
    }
}

// ---------------------------------------------------------------- Work

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
            style = heading(28.sp, if (hovered && link != null) Palette.Green else Palette.LightestSlate),
            textAlign = textAlign,
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                .then(if (link != null) Modifier.linkClick(source) { uri.openUri(link) } else Modifier),
        )
        Box(
            Modifier
                .then(if (desktop) Modifier.widthIn(max = 600.dp) else Modifier.fillMaxWidth())
                .shadow(12.dp, RoundedCornerShape(4.dp))
                .background(Palette.LightNavy, RoundedCornerShape(4.dp))
                .padding(25.dp),
        ) {
            Text(p.details, style = body(17.sp, Palette.LightSlate), textAlign = textAlign)
        }
        Text(
            p.stack.joinToString("    "),
            style = mono(13.sp, Palette.LightSlate),
            textAlign = textAlign,
            modifier = Modifier.padding(top = 25.dp),
        )
        if (link != null) {
            LinkText("View Project", link, mono(13.sp, Palette.LightestSlate), Modifier.padding(top = 10.dp))
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
            .background(Palette.LightNavy, RoundedCornerShape(4.dp))
            .linkClick(source) { link?.let { uri.openUri(it) } }
            .padding(horizontal = 28.dp, vertical = 30.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FolderIcon()
            Spacer(Modifier.weight(1f))
            if (link != null) LinkText("View", link, mono(12.sp, Palette.LightSlate))
        }
        Text(
            p.name,
            style = heading(20.sp, if (hovered) Palette.Green else Palette.LightestSlate),
            modifier = Modifier.padding(top = 28.dp, bottom = 10.dp),
        )
        Text(p.details, style = body(15.sp, Palette.LightSlate))
        Spacer(Modifier.weight(1f).heightIn(min = 20.dp))
        Text(p.stack.joinToString("   "), style = mono(12.sp, Palette.Slate))
    }
}

// ---------------------------------------------------------------- Contact & footer

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

@Composable
fun Footer(portfolio: Portfolio, showSocials: Boolean) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (showSocials) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                portfolio.socials.forEach { LinkText(it.label, it.url, mono(12.sp, Palette.LightSlate)) }
            }
        }
        val credit = "Designed & Built by ${portfolio.header.name}"
        val creditUrl = portfolio.socials.firstOrNull()?.url
        if (creditUrl != null) {
            LinkText(credit, creditUrl, mono(12.sp, Palette.Slate))
        } else {
            Text(credit, style = mono(12.sp, Palette.Slate))
        }
        Text("Rebuilt with Compose Multiplatform", style = mono(11.sp, Palette.Slate.copy(alpha = 0.7f)))
    }
}
