## NovaOS Master Checklist

### 1. AOSP & Device Setup
- [x] Choose primary reference device (Pixel / OnePlus / dev board)
- [x] Set up Linux build environment
    - [x] Install required packages (git, gnupg, flex, bison, gperf, build-essential, etc.)
    - [x] Install and configure `repo` tool
    - [x] Configure `ccache` for build speedup
- [x] Sync AOSP source
    - [x] `repo init` with desired Android version
    - [x] `repo sync -c -j$(nproc)`
- [x] `source build/envsetup.sh` works
- [x] `lunch <device>-userdebug` works
- [ ] Full build completes (`m` or `make`)
- [x] Create `device/<vendor>/<device>/` tree
- [x] Create `vendor/infynova/` vendor tree
- [x] Define `nova_<device>-userdebug` lunch target in `vendorsetup.sh`
- [x] Define `nova_<device>-user` lunch target
- [x] Script basic build command (`build.sh`)

### 2. Core Philosophy & Product
- [x] Write 3–5 non-negotiable rules:
  - [x] No ads anywhere in system
  - [x] All third-party apps uninstallable
  - [x] No auto-install “suggested/hot apps”
  - [x] Privacy safest by default
- [x] Write one-line product promise
- [x] Define target user persona (e.g., "The Privacy-Conscious Professional")
- [ ] List top 10 smartphone pain points from real users
- [ ] Map which pain points NovaOS will solve in v1 (The "Nova v1 Scope")

### 3. Branding & Visual Identity
- [ ] Design NovaOS logo (SVG & PNG formats)
- [x] Choose primary font and fallbacks (e.g., Inter, Roboto)
- [x] Define color palette
    - [x] Primary: #007fac
    - [x] Accent: #007fac
    - [x] Background/Neutral: #000000
- [ ] Create default light wallpaper
- [ ] Create default dark wallpaper
- [ ] Create boot animation frames with NovaOS logo
- [ ] Implement boot animation in build (`media/bootanimation.zip`)
- [ ] Create icon guidelines (Adaptive icons, specific corner radius)
- [x] Implement theme via overlay (`frameworks/base/core/res/res/values/colors.xml`)

### 4. System UI (SystemUI app)
- [ ] Clone/Fork SystemUI for reference
- [ ] Clean status bar icons
    - [ ] Remove redundant carrier labels
    - [ ] Minimalist battery and clock styles
- [ ] Simplify quick settings tiles (Remove default "bloat" tiles)
- [x] Add tile: Nova AI (Custom `TileService`)
- [x] Add tile: Privacy mode
- [x] Add tile: Performance mode
- [ ] Adjust notification shade layout (Spacing, transparency, blur)
- [ ] Add visual indicators for mic/camera/location use (Privacy chips)
- [ ] Test orientation, notch/punch-hole layouts on target device

### 5. Home Screen / Launcher
- [x] Decide: use Launcher3 fork vs custom launcher
- [x] Remove any “hot apps” / promoted folders from default workspace
- [x] Implement basic home grid (configurable 4x4, 5x5)
- [x] Implement app drawer with search (Focus on speed)
- [x] Implement long-press actions (uninstall, app info, widgets)
- [x] Default home: no third-party widgets pre-placed
- [x] Add NovaOS-branded wallpaper as default in `frameworks/base/core/res/res/drawable`
- [x] Test app install/uninstall flow from drawer

### 6. Settings App
- [x] Reorganize main categories in `res/xml/top_level_settings.xml`
- [x] Add “Nova Privacy & Security” section
- [x] Add “Nova AI & Intelligence” section
- [x] Add “Nova Device Care” section
- [x] Remove any promotional banners or "Suggestions"
- [x] Simplify wording of key settings (Plain English)
- [x] Ensure search in settings works for new items (Update `SearchIndexProvider`)

### 7. Essential System Apps
- [x] Phone app (Clean, no-ad dialer)
- [x] SMS app (Simple, secure)
- [x] Contacts
- [x] Camera (Minimal UI, focus on quality)
- [x] Gallery (Offline-first, no cloud sync by default)
- [x] Browser (Privacy-focused, built-in ad blocking)
- [x] File manager (Clear storage visualization)
- [x] Ensure: no ads, no forced logins in any defaults
- [ ] Ensure: all non-essential third-party apps are removable (`PRODUCT_PACKAGES` audit)

### 8. NovaPrivacyCenter (App)
- [x] Create `packages/apps/NovaPrivacyCenter/`
- [x] Screen: list apps with sensitive permissions (Camera, Mic, Location)
- [x] Screen: internet access control per app (Firewall)
- [ ] Screen: mic/camera/location usage history (Timeline)
- [ ] Action: open app details / revoke permissions
- [ ] Action: shortcut to disable background activity for specific apps
- [ ] Add shortcut in Settings → Privacy
- [x] Add Quick Settings shortcut to open PrivacyCenter

### 9. NovaAI (App / Service)
- [x] Create `packages/apps/NovaAI/`
- [x] Implement offline command handler (System control via voice/text)
- [x] Implement simple text actions (summarize, quick reply MVP using on-device ML)
- [x] Create minimal UI (chat-like floating window or screen)
- [x] Add Quick Settings tile for NovaAI
- [x] Ensure: on-device by default, no cloud dependency for core features

### 10. NovaDeviceCare
- [x] Create `packages/apps/NovaDeviceCare/`
- [x] Battery page (Detailed drain stats, "Deep Sleep" management)
- [ ] Storage page (Identify duplicate files, large media)
- [x] Performance profiles (Balanced / Performance / Battery saver)
- [ ] One-tap checkup (System health overview)
- [ ] Integrate into Settings → Nova Device Care

### 11. NO Bloat Implementation
- [x] Audit `PRODUCT_PACKAGES` in `build/target/product/`
- [x] Remove shopping, social, content partner apps from the build
- [x] Ensure no OEM-style app stores (Only provide essential FOSS or Core apps)
- [ ] Make all non-core third-party packages uninstallable (`core="false"`)
- [ ] Test clean install: check app list feels minimal (Less than 25 apps)

### 12. NO Ads Implementation
- [x] Search codebase for ad SDKs (AdMob, etc.) and remove
- [x] Ensure:
  - [x] No lockscreen ads or "magazines"
  - [x] No notification shade ads or "recommendations"
  - [x] No ads inside system apps (Settings, Files, etc.)
- [x] No “Lock Glimpse” / “Glance” style layers
- [x] Verify: no background “recommendation” services run (`grep` for "recommend")

### 13. Onboarding / Setup
- [x] Simple setup wizard (Language, Wi‑Fi, Timezone)
- [x] Add Nova privacy choices:
  - [x] Minimal data (Local only)
  - [x] Standard
  - [ ] Custom
- [ ] Add optional “recommended apps” page (All toggles OFF by default)
- [x] Explain NovaOS values (No Ads, Privacy) on one screen
- [ ] Ensure: no forced sign-up for Nova account

### 14. Notifications & Dark Patterns
- [ ] Default promotional notification categories OFF in `NotificationManagerService`
- [ ] Clear notification channels for system apps (Rename for clarity)
- [ ] No “rate us now” nagging loops in system apps
- [ ] No periodic “clean device / boost RAM” spam
- [ ] Make all suggestions dismissable and not auto-return

### 15. Privacy & Security Deep
- [ ] Implement privacy dashboard (System-level view of data access)
- [ ] Mic/camera/location indicators in status bar (Persistent vs. Toast)
- [ ] Quick way to revoke recently used permissions from notification shade
- [ ] Storage encryption enabled by default (`fileencryption=aes-256-xts`)
- [ ] Strong lockscreen flows (PIN/pattern/password/biometric)
- [ ] Make lockscreen notifications privacy-aware (Content hidden by default)
- [x] Document data handling philosophy (The "Nova Privacy Manifesto")

### 16. Performance & Battery
- [ ] Review running services; disable non-essential AOSP background tasks
- [ ] Tune animation scales (0.5x default for snappiness)
- [ ] Tune LMK (Low Memory Killer) settings for the specific device RAM
- [ ] Perform battery test with typical day usage (Idle drain < 1% per hour)
- [ ] Optimize wakeups and alarms for Nova apps (JobScheduler optimization)
- [ ] Benchmark vs stock build (Boot time, App launch speed)

### 17. Stability & Bugfix Loop
- [ ] Enable useful log tags for debugging in `SystemServer`
- [ ] Create small internal “feedback” app to collect logs
- [ ] Test:
  - [ ] Calls, SMS, VoLTE/VoWiFi
  - [ ] Camera (Photo, Video, HDR)
  - [ ] Wi‑Fi (2.4/5GHz), Mobile Data (LTE/5G)
  - [ ] Bluetooth (Audio, File Transfer, LE)
  - [ ] Sensors (Brightness, Rotation, Proximity)
- [ ] Maintain a `BUGS.md` with known issues
- [ ] Fix crashes from logs and user reports regularly

### 18. Documentation & Developer Experience
- [x] `README.md` (What is NovaOS, how to build)
- [x] `PRINCIPLES.md` (No ads, no bloat, privacy-first)
- [x] `ARCHITECTURE.md` (Apps and system layout)
- [x] `FEATURES.md` (What’s different from stock)
- [x] `ROADMAP.md` (0.1, 0.2, 0.3 goals)
- [ ] `CHANGELOG.md` (Version history)
- [ ] `CONTRIBUTING.md` (How others should contribute)

### 19. Demo & Launch Prep
- [ ] Prepare demo script (Boot → Home → Privacy → NovaAI → No Ads)
- [ ] Record screen + physical camera demo
- [ ] Capture key screenshots for marketing
- [ ] Write brief product story for LinkedIn / YouTube
- [ ] Identify 5–10 early testers
- [ ] Collect feedback and prioritize fixes for public beta

### 20. Business & Future
- [ ] List possible revenue streams (Hardware margin, Optional B2B services)
- [ ] Explicitly reject ad-based and lockscreen monetization models
- [ ] Outline future premium features that still respect user (e.g., Nova+ AI)
- [ ] Plan update policy (e.g., 3 years of OS updates, 5 years security)
