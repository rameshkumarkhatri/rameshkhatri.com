package com.rameshkhatri.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rameshkhatri.portfolio.data.Portfolio
import com.rameshkhatri.portfolio.data.loadPortfolio
import com.rameshkhatri.portfolio.designsystem.PortfolioTheme
import com.rameshkhatri.portfolio.designsystem.ThemeState
import com.rameshkhatri.portfolio.designsystem.isDark
import com.rameshkhatri.portfolio.ui.components.AmbientBackground
import com.rameshkhatri.portfolio.ui.components.EmailRail
import com.rameshkhatri.portfolio.ui.components.MobileMenu
import com.rameshkhatri.portfolio.ui.components.NavBar
import com.rameshkhatri.portfolio.ui.components.NavHeight
import com.rameshkhatri.portfolio.ui.components.NavHeightScrolled
import com.rameshkhatri.portfolio.ui.components.RevealOnScroll
import com.rameshkhatri.portfolio.ui.components.SocialRail
import com.rameshkhatri.portfolio.ui.sections.AboutSection
import com.rameshkhatri.portfolio.ui.sections.ContactSection
import com.rameshkhatri.portfolio.ui.sections.ExperienceSection
import com.rameshkhatri.portfolio.ui.sections.Footer
import com.rameshkhatri.portfolio.ui.sections.HeroSection
import com.rameshkhatri.portfolio.ui.sections.WorkSection
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class Section(val label: String) {
    About("About"),
    Experience("Experience"),
    Work("Work"),
    Contact("Contact"),
}

@Composable
fun App() {
    LaunchedEffect(Unit) { ThemeState.load() }
    val dark = ThemeState.mode.isDark()
    PortfolioTheme(darkTheme = dark, colors = ThemeState.colors(dark)) {
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
    var pointer by remember { mutableStateOf<Offset?>(null) }
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(PortfolioTheme.colors.background)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        when (event.type) {
                            PointerEventType.Move, PointerEventType.Enter -> pointer = event.changes.first().position
                            PointerEventType.Exit -> pointer = null
                        }
                    }
                }
            }
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        AmbientBackground(pointer, Modifier.fillMaxSize())
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
                desktop = desktop,
                scroll = { scroll.value },
                pointer = { pointer },
                onGetInTouch = {
                    if (portfolio.getInTouchUrl.isNotBlank()) uri.openUri(portfolio.getInTouchUrl)
                    else navigate(Section.Contact)
                },
            )
            RevealOnScroll(Modifier.anchor(Section.About)) {
                AboutSection(portfolio.about, portfolio.header.initial, desktop)
            }
            if (portfolio.experience.isNotEmpty()) {
                RevealOnScroll(Modifier.anchor(Section.Experience)) { ExperienceSection(portfolio.experience, desktop) }
            }
            if (portfolio.featuredProjects.isNotEmpty() || portfolio.caseStudies.isNotEmpty()) {
                RevealOnScroll(Modifier.anchor(Section.Work)) {
                    WorkSection(portfolio.featuredProjects, portfolio.caseStudies, desktop)
                }
            }
            RevealOnScroll(Modifier.anchor(Section.Contact)) { ContactSection(portfolio, desktop) }
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
                isDark = isDark,
                onNavigate = ::navigate,
                onResume = { uri.openUri(portfolio.resumeUrl) },
            )
        }
    }
}
