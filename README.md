# rameshkhatri.com — Compose Multiplatform

A port of the Gatsby site at rameshkhatri.com to Compose Multiplatform. One shared UI in
`commonMain` runs on **Android, iOS, Desktop (macOS / Windows / Linux) and the Web (Wasm)**.

## What's in it

The same single-page layout as the original site, in the same navy / mint palette:

- Hero with a staggered fade-in
- 01. About Me — bio, skills list, framed portrait placeholder
- 02. Where I've Worked — company tabs (vertical on wide screens, scrollable on phones)
- 03. Some Things I've Built — featured projects plus a grid of other projects
- 04. Get In Touch — mailto button
- Top nav that shrinks when you scroll, hexagon logo, Resume button
- Social links and email on vertical side rails (wide screens), slide-in menu (phones)
- Hover states on desktop and web; nav links scroll smoothly to each section

## Editing content

All content lives in `composeApp/src/commonMain/composeResources/files/portfolio.json`
(header, contact links, about, experience, featured projects, case studies). It is loaded at
startup and parsed into the models in `data/Portfolio.kt`. Optional fields (`resume_url`,
`location`, `url`, `link_url`, …) can be left out or blank and the matching UI is hidden.

Colors, typography and the theme live in the `designsystem` module.

**Themes are data.** `designsystem/src/commonMain/composeResources/files/themes.json` defines
the five built-in themes (Midnight, Ember, Forest, Rose, Graphite), each with a `dark` and a
`light` variant and a `default` id. Every variant supplies the same seven slots as `#RRGGBB`
(or `#AARRGGBB`) strings: `background`, `surface`, `outline`, `text_primary`, `text_secondary`,
`text_muted`, `accent`; `scrim` is optional. Add an object to the `themes` array to add a theme;
it appears in the "05. Theme" nav tab automatically. Keep body text (`text_muted`) at 4.5:1 or
better against `background`.

Code, in `designsystem/src/commonMain/kotlin/com/rameshkhatri/portfolio/designsystem/`:

- `ThemeCatalog.kt` — JSON models (`ThemeSpec`, `ColorSpec`) and `loadThemeCatalog()`
- `Colors.kt` — `PortfolioColors` semantic slots, plus the built-in Midnight palettes used until
  the catalog loads
- `Typography.kt` — `mono()`, `body()`, `heading()` text styles (bundled Fira Code)
- `Theme.kt` — `PortfolioTheme(...)` root, `PortfolioTheme.colors` accessor, and `ThemeState`
  (selected theme id + `ThemeMode` System/Light/Dark; plain Kotlin so Swift can drive it too)

The nav bar has a sun/moon toggle for light/dark and a "Theme" tab (dropdown on wide screens,
swatch row in the phone menu). Selection is not persisted yet; it resets on relaunch.

## UI layout

`composeApp/src/commonMain/kotlin/com/rameshkhatri/portfolio/`:

| Path | Contents |
|---|---|
| `App.kt` | `App()` root, `PortfolioPage` composition, `Section` enum |
| `data/` | `portfolio.json` models and loader |
| `ui/sections/` | One file per page section: Hero, About, Experience, Work, Contact, Footer |
| `ui/components/` | One file per reusable component: NavBar, MobileMenu, SideRails, Logo, MenuIcon, ThemeToggle, ThemeSwatch, OutlineButton, LinkText, Bullet, SectionHeading, FolderIcon, Reveal |
| `ui/utils/` | Modifier and animation helpers: `Hover.kt` (`rememberHoverSource`, `linkClick`), `Layout.kt` (`verticalText`), `Animation.kt` (`animateLift`) |

## Running

Requires JDK 17+ and Android Studio (or IntelliJ IDEA) with the Kotlin Multiplatform plugin.

| Platform | Command |
|---|---|
| Desktop | `./gradlew :composeApp:run` |
| Web (dev server) | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` |
| Web (production) | `./gradlew :composeApp:wasmJsBrowserDistribution` → `composeApp/build/dist/wasmJs/productionExecutable/` |
| Android | `./gradlew :composeApp:installDebug`, or run `composeApp` from Android Studio |
| iOS | see below |

### iOS

`iosApp/iosApp/` has the SwiftUI entry point (`iOSApp.swift`, `ContentView.swift`). The Xcode
project file isn't included. The quickest way to get one:

1. Generate a project at https://kmp.jetbrains.com with iOS selected and "Share UI" on.
2. Copy its `iosApp/` folder (with `iosApp.xcodeproj`) into this repo.
3. Replace its `ContentView.swift` with the one here. The framework name is `ComposeApp`, same as the template.
4. Open `iosApp/iosApp.xcodeproj` in Xcode and run.

## Deploying the web version

`.github/workflows/deploy-web.yml` builds the Wasm site and publishes it to GitHub Pages on
every push to `main`. Point the rameshkhatri.com DNS at GitHub Pages (add a `CNAME` file with
`rameshkhatri.com` to `composeApp/src/wasmJsMain/resources/`) to replace the Gatsby site.
Compose for Web needs a browser with WasmGC support (current Chrome, Firefox, Safari 18.2+, Edge).

## To do

- **Photo:** the About section shows "RK" initials. Add your headshot to
  `composeApp/src/commonMain/composeResources/drawable/` (add `implementation(compose.components.resources)`
  to `commonMain`) and swap the `Text("RK")` in `Portrait()` for an `Image`.
- **Fonts:** the original uses Calibre and SF Mono. Fira Code is bundled for monospace; body text
  uses the platform default. Drop `.ttf` files into `designsystem/src/commonMain/composeResources/font/`
  and update `designsystem/.../Typography.kt` to match exactly.

## Versions

Kotlin 2.1.21 · Compose Multiplatform 1.8.2 · AGP 8.9.3 · Gradle 8.14.3 (see `gradle/libs.versions.toml`).
