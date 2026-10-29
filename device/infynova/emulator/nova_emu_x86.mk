# NovaOS Basic Emulator Product Configuration - x86 (32-bit, Lightweight)

$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_x86.mk)

# NovaOS Branding & Overlays
DEVICE_PACKAGE_OVERLAYS += device/infynova/nova1/overlay

# Boot Animation
PRODUCT_COPY_FILES += \
    device/infynova/nova1/assets/bootanimation.zip:$(TARGET_COPY_OUT_PRODUCT)/media/bootanimation.zip

# Essential Product Packages ONLY (Reduced RAM/CPU Stress)
PRODUCT_PACKAGES += \
    NovaLauncher \
    NovaSettings

# System Properties for Lightweight Emulator
PRODUCT_PROPERTY_OVERRIDES += \
    ro.nova.version=v1.0-basic-emu \
    ro.nova.device=emulator_x86_basic \
    ro.setupwizard.mode=DISABLED \
    persist.sys.nova.optimize_apps=true \
    persist.sys.nova.background_limit=true \
    hwui.disable_vsync=true \
    debug.hwui.renderer=opengl

PRODUCT_NAME := nova_emu_x86
PRODUCT_DEVICE := generic_x86
PRODUCT_BRAND := InfyNova
PRODUCT_MODEL := NovaOS Basic Emulator (32-bit)
