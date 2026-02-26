# Implementation Plan - Advanced Customization Center (Phase 3)

Final polish and production readiness for the Advanced Customization Center.

## Proposed Changes

### [Component] Settings (Advanced Customization Center)
Create a new fragment and XML for customization options.

#### [NEW] [CustomizationSettingsFragment.java](file:///home/robin/novaos/packages/apps/Settings/src/com/android/settings/nova/CustomizationSettingsFragment.java)
#### [NEW] [customization_settings.xml](file:///home/robin/novaos/packages/apps/Settings/res/xml/customization_settings.xml)
#### [MODIFY] [nova_settings.xml](file:///home/robin/novaos/packages/apps/Settings/res/xml/nova_settings.xml)
- Add a link to the Advanced Customization Center.

---

### [Component] Status Bar Plus
Implement Battery Bar and icon toggles.

#### [NEW] [BatteryBarView.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryBarView.java)
- A custom view that draws a battery progress bar at the top of the status bar.
#### [MODIFY] [PhoneStatusBarView.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/phone/PhoneStatusBarView.java)
- Add `BatteryBarView` to the layout and manage its visibility.
#### [MODIFY] [StatusBarIconControllerImpl.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/phone/StatusBarIconControllerImpl.java)
- Handle disabling icons (NFC, Bluetooth, Data) based on settings.

---

### [Component] Quick Settings Pro
Implement grid customization and brightness slider position.

#### [MODIFY] [QSPanel.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/qs/QSPanel.java)
- Handle brightness slider position (Top/Bottom).
#### [MODIFY] [PagedTileLayout.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/qs/PagedTileLayout.java)
- Handle grid customization (max columns up to 4).

---

### [Component] Lockscreen Mods
Implement clock fonts and double-tap to sleep.

#### [MODIFY] [KeyguardClockSwitch.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/keyguard/KeyguardClockSwitch.java)
- Apply custom fonts to the clock view.
#### [MODIFY] [NotificationPanelViewController.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/phone/NotificationPanelViewController.java)
- Implement double-tap to sleep on the lockscreen empty area.

---

### [Component] Navigation & Gestures
Implement pill customization and volume key skip tracks.

#### [MODIFY] [NavigationHandle.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/navigationbar/gestural/NavigationHandle.java)
- Use settings for pill length and thickness.
#### [MODIFY] [PhoneWindowManager.java](file:///home/robin/novaos/frameworks/base/services/core/java/com/android/server/policy/PhoneWindowManager.java)
- Implement volume key skip track logic when screen is off.
---

### [Component] Network Traffic Monitor [NEW]
Show real-time upload/download speeds in the status bar.

#### [NEW] [NetworkTrafficView.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkTrafficView.java)
- Logic to track network usage and update text.
#### [MODIFY] [status_bar.xml](file:///home/robin/novaos/frameworks/base/packages/SystemUI/res/layout/status_bar.xml)
- Add the `NetworkTrafficView` to the center or right of the status bar.

---

### [Component] QS Header Customization [NEW]
#### [MODIFY] [QSPanel.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/qs/QSPanel.java)
- Support for custom header images or gradients.

---
---

### [Component] Battery Bar Enhancements [NEW]
#### [MODIFY] [BatteryBarView.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/policy/BatteryBarView.java)
- Support custom colors and thickness via settings.
#### [MODIFY] [customization_settings.xml](file:///home/robin/novaos/packages/apps/Settings/res/xml/customization_settings.xml)
- Add color picker and thickness slider.

---

### [Component] QS Header Visual Assets [NEW]
#### [NEW] [qs_header_abstract.png](file:///home/robin/novaos/packages/apps/Settings/res/drawable/qs_header_abstract.png)
- Custom generated abstract background.
#### [NEW] [qs_header_nature.png](file:///home/robin/novaos/packages/apps/Settings/res/drawable/qs_header_nature.png)
- Custom generated nature background.
#### [MODIFY] [QuickStatusBarHeader.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/qs/QuickStatusBarHeader.java)
- Map setting indices to actual drawable resources.

---

### [Component] Network Traffic Monitor Units [NEW]
#### [MODIFY] [NetworkTrafficView.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkTrafficView.java)
- Support for different units (B/s, KB/s, MB/s) and auto-scaling.

## Verification Plan

### Phase 3 Verification
- [ ] Verify Battery Bar color changes dynamically.
- [ ] Verify all QS Header assets load correctly.
- [ ] Verify Traffic Monitor scales units properly.

### [Component] Power Menu Enhancements [NEW]
#### [MODIFY] [GlobalActionsDialogLite.java](file:///home/robin/novaos/frameworks/base/packages/SystemUI/src/com/android/systemui/globalactions/GlobalActionsDialogLite.java)
- Implement Advanced Reboot (Recovery, Bootloader).

## Verification Plan

### Automated Tests
- Run `atest SystemUITests` to ensure no regressions in SystemUI.
- Run `atest SettingsTests` for Settings app.

### Manual Verification
1.  Open **Settings > NovaOS > Advanced Customization**.
2.  Enable **Battery Bar** and verify it appears at the top.
3.  Toggle **NFC/Bluetooth/Data** icons and verify they disappear/appear in the status bar.
4.  Change **QS Grid** to 4x4 and verify the layout change.
5.  Change **Brightness slider** position and verify it moves.
6.  Change **Clock font** on lockscreen and verify.
7.  **Double-tap** on lockscreen to sleep.
8.  Adjust **Gesture Pill** length and thickness.
9.  **Long-press** volume buttons with screen off to skip tracks.
