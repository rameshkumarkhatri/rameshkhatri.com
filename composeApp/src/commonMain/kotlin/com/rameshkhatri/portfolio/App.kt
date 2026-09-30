package com.rameshkhatri.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.data.loadPortfolio
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.ThemeState
import com.rameshkhatri.portfolio.designsystem.isDark
import com.rameshkhatri.portfolio.designsystem.mono
import com.rameshkhatri.portfolio.ui.AboutSection
import com.rameshkhatri.portfolio.ui.ContactSection
import com.rameshkhatri.portfolio.ui.ExperienceSection
import com.rameshkhatri.portfolio.ui.Footer
import com.rameshkhatri.portfolio.ui.HeroSection
import com.rameshkhatri.portfolio.ui.LinkText
import com.rameshkhatri.portfolio.ui.Logo
import com.rameshkhatri.portfolio.ui.MenuIcon
import com.rameshkhatri.portfolio.ui.OutlineButton
import com.rameshkhatri.portfolio.ui.Reveal
import com.rameshkhatri.portfolio.ui.ThemeToggle
import com.rameshkhatri.portfolio.ui.WorkSection
import com.rameshkhatri.portfolio.ui.linkClick
import com.rameshkhatri.portfolio.ui.rememberHoverSource
import com.rameshkhatri.portfolio.ui.verticalText
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class Section(val label: String) {
    About("About"),
    Experience("Experience"),
    Work("Work"),
    Contact("Contact"),
}

private val NavHeight = 80.dp
private val NavHeightScrolled = 70.dp

@Composable
fun App() {
    val dark = ThemeState.mode.isDark()
    PortfolioTheme(darkTheme = dark) {
        val portfolio by produceState<Portfolio?>(null) { value = loadPortfolio() }
        val content = portfolio
        if (content == null) {
            Box(Modifier.fillMaxSize().background(PortfolioTheme.colors.background))
        } else {
            PortfolioPage(content, isDark = dark, onToggleTheme = { ThemeState.toggle(dark) })
        }
    }
}

@Composable
private fun PortfolioPage(portfolio: Portfolio, isDark: Boolean, onToggleTheme: () -> Unit) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(PortfolioTheme.colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        val width = maxWidth
        val height = maxHeight
        val desktop = width > 768.dp
        val hPad = when {
            width > 1080.dp -> 150.dp
            width > 768.dp -> 100.dp
            width > 480.dp -> 50.dp
            else -> 25.dp
        }

        val scroll = rememberScrollState()
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current
        val uri = LocalUriHandler.current
        val anchors = remember { mutableStateMapOf<Section, Int>() }
        var menuOpen by remember { mutableStateOf(false) }
        val scrolled by remember { derivedStateOf { scroll.value > 0 } }

        fun scrollTo(target: Int) {
            menuOpen = false
            scope.launch { scroll.animateScrollTo(target.coerceAtLeast(0)) }
        }

        fun navigate(section: Section) {
            val y = anchors[section] ?: return
            scrollTo(y - with(density) { NavHeightScrolled.roundToPx() })
        }

        fun Modifier.anchor(section: Section) = onGloballyPositioned {
            anchors[section] = it.positionInParent().y.roundToInt()
        }

        // Page content
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = hPad),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeroSection(
                header = portfolio.header,
                contentWidth = width - hPad * 2,
                minHeight = height,
                topPadding = NavHeight,
                onGetInTouch = {
                    if (portfolio.getInTouchUrl.isNotBlank()) uri.openUri(portfolio.getInTouchUrl)
                    else navigate(Section.Contact)
                },
            )
            Box(Modifier.anchor(Section.About)) { AboutSection(portfolio.about, portfolio.header.initial, desktop) }
            if (portfolio.experience.isNotEmpty()) {
                Box(Modifier.anchor(Section.Experience)) { ExperienceSection(portfolio.experience, desktop) }
            }
            if (portfolio.featuredProjects.isNotEmpty() || portfolio.caseStudies.isNotEmpty()) {
                Box(Modifier.anchor(Section.Work)) {
                    WorkSection(portfolio.featuredProjects, portfolio.caseStudies, desktop)
                }
            }
            Box(Modifier.anchor(Section.Contact)) { ContactSection(portfolio, desktop) }
            Footer(portfolio, showSocials = !desktop)
        }

        if (desktop) {
            SocialRail(portfolio, Modifier.align(Alignment.BottomStart).padding(start = 40.dp))
            if (portfolio.email.isNotBlank()) {
                EmailRail(portfolio.email, Modifier.align(Alignment.BottomEnd).padding(end = 40.dp))
            }
        }

        NavBar(
            desktop = desktop,
            scrolled = scrolled,
            menuOpen = menuOpen,
            initial = portfolio.header.initial,
            showResume = portfolio.resumeUrl.isNotBlank(),
            onLogo = { scrollTo(0) },
            onNavigate = ::navigate,
            onResume = { uri.openUri(portfolio.resumeUrl) },
            onMenu = { menuOpen = !menuOpen },
            isDark = isDark,
            onToggleTheme = onToggleTheme,
        )

        if (!desktop) {
            MobileMenu(
                open = menuOpen,
                onClose = { menuOpen = false },
                showResume = portfolio.resumeUrl.isNotBlank(),
                onNavigate = ::navigate,
                onResume = { uri.openUri(portfolio.resumeUrl) },
            )
        }
    }
}

@Composable
private fun NavBar(
    desktop: Boolean,
    scrolled: Boolean,
    menuOpen: Boolean,
    initial: String,
    showResume: Boolean,
    onLogo: () -> Unit,
    onNavigate: (Section) -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val height by animateDpAsState(if (scrolled) NavHeightScrolled else NavHeight)
    Row(
        Modifier
            .fillMaxWidth()
            .height(height)
            .then(if (scrolled) Modifier.shadow(10.dp) else Modifier)
            .background(PortfolioTheme.colors.background.copy(alpha = 0.95f))
            .padding(horizontal = if (desktop) 50.dp else 25.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (logoSource, _) = rememberHoverSource()
        Reveal(0) { Logo(initial, Modifier.linkClick(logoSource, onLogo)) }
        Spacer(Modifier.weight(1f))
        if (desktop) {
            Section.entries.forEachIndexed { i, s ->
                Reveal(i + 1) { NavLink(i + 1, s.label) { onNavigate(s) } }
            }
            Reveal(Section.entries.size + 1) { ThemeToggle(isDark, onToggleTheme) }
            Spacer(Modifier.width(15.dp))
            if (showResume) {
                Reveal(Section.entries.size + 2) { OutlineButton("Resume", onClick = onResume) }
            }
        } else {
            ThemeToggle(isDark, onToggleTheme)
            Spacer(Modifier.width(8.dp))
            val (menuSource, _) = rememberHoverSource()
            MenuIcon(menuOpen, Modifier.linkClick(menuSource, onMenu))
        }
    }
}

@Composable
private fun NavLink(number: Int, label: String, onClick: () -> Unit) {
    val (source, hovered) = rememberHoverSource()
    Row(Modifier.linkClick(source, onClick).padding(10.dp)) {
        Text("0$number.", style = mono(13.sp))
        Text(" $label", style = mono(13.sp, if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary))
    }
}

@Composable
private fun MobileMenu(
    open: Boolean,
    onClose: () -> Unit,
    showResume: Boolean,
    onNavigate: (Section) -> Unit,
    onResume: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(open, enter = fadeIn(), exit = fadeOut()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(PortfolioTheme.colors.scrim)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClose),
            )
        }
        AnimatedVisibility(
            open,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().fillMaxWidth(0.75f),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shadow(20.dp)
                    .background(PortfolioTheme.colors.surface)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            ) {
                val (closeSource, _) = rememberHoverSource()
                MenuIcon(
                    open = true,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 28.dp, end = 25.dp)
                        .linkClick(closeSource, onClose),
                )
                Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(36.dp),
                ) {
                    Section.entries.forEachIndexed { i, s ->
                        val (source, hovered) = rememberHoverSource()
                        Column(
                            Modifier.linkClick(source) { onNavigate(s) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("0${i + 1}.", style = mono(14.sp))
                            Text(
                                s.label,
                                style = mono(18.sp, if (hovered) PortfolioTheme.colors.accent else PortfolioTheme.colors.textPrimary),
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                    if (showResume) OutlineButton("Resume", big = true, onClick = onResume)
                }
            }
        }
    }
}

@Composable
private fun SocialRail(portfolio: Portfolio, modifier: Modifier) {
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
private fun EmailRail(email: String, modifier: Modifier) {
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
