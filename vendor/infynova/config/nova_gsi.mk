# NovaOS Generic System Image (GSI) Configuration
# This file is used to build a universal system.img for Treble-compliant devices.

# Inherit from standard AOSP GSI base
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_arm64.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/gsi_release.mk)

# Include NovaOS Identity and Core Features
$(call inherit-product, vendor/infynova/config/common.mk)

PRODUCT_NAME := nova_gsi_arm64
PRODUCT_DEVICE := generic_arm64
PRODUCT_BRAND := NovaOS
PRODUCT_MODEL := NovaOS Universal GSI

# GSI-specific Overrides
# GSIs usually don't include many vendor-specific blobs to ensure compatibility
PRODUCT_PACKAGES += \
    NovaLauncher \
    NovaAIService \
    NovaSystemUI \
    NovaSettings \
    NovaPrivacyCenter \
    NovaDeviceCare \
    NovaSetup \
    NovaUpdater \
    novaid

# Properties for GSI identity
PRODUCT_PROPERTY_OVERRIDES += \
    ro.nova.version=1.0.0-gsi \
    ro.nova.release.type=universal \
    ro.system.ota.json_url=https://ota.infynova.com/gsi/updates.json

# Enable Treble requirements
PRODUCT_FULL_TREBLE_OVERRIDE := true
PRODUCT_COMPATIBLE_PROPERTY_OVERRIDE := true

# Adjustments for GSI compatibility (Common community fixes)
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.phh.main_buttons=0 \
    persist.sys.phh.backlight.scale=1
