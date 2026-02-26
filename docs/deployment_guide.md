# NovaOS Deployment & Testing Guide

This guide explains how to build and test your NovaOS customizations on an emulator or real device.

## 1. Build Environment Setup
Ensure you have the AOSP build dependencies installed and your terminal is initialized:
```bash
# Initialize the build environment
source build/envsetup.sh

# Select the target device (e.g., for emulator)
lunch aosp_arm64-eng # Or your specific NovaOS target
```

## 2. Full System Build
To build the complete system image:
```bash
mka bacon -j$(nproc)
```
*Note: `bacon` is a common target for custom ROMs; use the correct target for NovaOS.*

## 3. Testing on Emulator
If you are using the Android Emulator:
```bash
emulator
```
Wait for the device to boot, then navigate to **Settings > NovaOS Settings > Advanced Customization** to see your changes.

## 4. Testing on Real Device
1. **Flash via Fastboot**:
   ```bash
   adb reboot bootloader
   fastboot flash system system.img
   fastboot reboot
   ```
2. **Pushing Incremental Changes (Rapid Testing)**:
   If you only changed SystemUI or Settings and don't want to wait for a full build:
   ```bash
   # Build just SystemUI
   make SystemUI

   # Push to device (requires root/remount)
   adb root
   adb remount
   adb push $OUT/system/priv-app/SystemUI/SystemUI.apk /system/priv-app/SystemUI/
   adb shell stop
   adb shell start
   ```

## 5. Verification Checklist
- [ ] **Settings**: Open "Advanced Customization" and toggle features.
- [ ] **SystemUI**: Verify the Battery Bar, Network Traffic, and QS Header update instantly.
- [ ] **Gestures**: Test skip tracks and pill scaling in various apps.
- [ ] **Power Menu**: Long-press/Press the restart button to check the Recovery/Bootloader options.
