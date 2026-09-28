package com.rameshkhatri.portfolio.data

import com.rameshkhatri.portfolio.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

/*
 * All site content lives in composeResources/files/portfolio.json. Edit that file to
 * update the portfolio on every platform at once; these classes mirror its structure.
 */

@Serializable
data class Portfolio(
    val header: Header,
    @SerialName("get_in_touch_url") val getInTouchUrl: String = "",
    @SerialName("resume_url") val resumeUrl: String = "",
    val email: String = "",
    val github: String = "",
    val linkedin: String = "",
    val twitter: String = "",
    val about: About,
    val experience: List<Experience> = emptyList(),
    @SerialName("featured_projects") val featuredProjects: List<Project> = emptyList(),
    @SerialName("case_study") val caseStudies: List<Project> = emptyList(),
    val contact: Contact = Contact(),
) {
    /** Social links in display order; blank entries are skipped. */
    val socials: List<Link>
        get() = listOf(
            Link("GitHub", github),
            Link("LinkedIn", linkedin),
            Link("Twitter", twitter),
        ).filter { it.url.isNotBlank() }
}

@Serializable
data class Header(
    val initial: String,
    val name: String,
    @SerialName("short_desc") val shortDesc: String,
    @SerialName("long_desc") val longDesc: String,
)

@Serializable
data class About(
    val title: String = "About Me",
    val details: List<String> = emptyList(),
    @SerialName("tech_stack") val techStack: List<String> = emptyList(),
)

@Serializable
data class Experience(
    val title: String,
    val company: String,
    @SerialName("date_from") val dateFrom: String,
    @SerialName("date_to") val dateTo: String = "Present",
    val location: String = "",
    val url: String? = null,
    val bullets: List<String> = emptyList(),
) {
    val range: String get() = "$dateFrom — $dateTo"
}

@Serializable
data class Project(
    val name: String,
    val details: String,
    val stack: List<String> = emptyList(),
    @SerialName("link_url") val linkUrl: String? = null,
)

@Serializable
data class Contact(
    val title: String = "Get In Touch",
    val message: String = "",
)

data class Link(val label: String, val url: String)

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalResourceApi::class)
suspend fun loadPortfolio(): Portfolio =
    json.decodeFromString(Portfolio.serializer(), Res.readBytes("files/portfolio.json").decodeToString())
