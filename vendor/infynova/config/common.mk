# NovaOS Global Build Configuration
# Included by device makefiles to pull in InfYNova features.

# Native Components
PRODUCT_PACKAGES += \
    novaid \
    nova_slm_weights \
    com.infynova.novaid \
    com.infynova.novaid-java \
    com.infynova.novaid-ndk

# System Apps
PRODUCT_PACKAGES += \
    NovaLauncher \
    NovaAIService \
    NovaSystemUI \
    NovaSettings \
    NovaPrivacyCenter \
    NovaDeviceCare \
    NovaSetup \
    NovaUpdater

# Theme Overlays
PRODUCT_PACKAGES += \
    com.novaos.overlay.accent.nova \
    com.novaos.overlay.accent.indigo \
    com.novaos.overlay.accent.navy \
    com.novaos.overlay.accent.crimson

# Framework Extensions
# NovaGuardianService is compiled into services.jar,
# so it doesn't need a separate package entry,
# but we ensure the framework-res overlays are here.
PRODUCT_PACKAGE_OVERLAYS += vendor/infynova/overlay

# Security & Privacy Flags
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.nova.guardian.enabled=true \
    ro.nova.auth.url=https://auth.infynova.com/v1 \
    persist.sys.nova.ai_enabled=true

# Dynamic Island Support
PRODUCT_COPY_FILES += \
    vendor/infynova/assets/dynamic_island_mask.png:system/etc/nova/island_mask.png
