# NovaOS GMS / MicroG Strategy

To balance user convenience and privacy, NovaOS will support two build variants:

## 1. NovaOS-Core (Privacy Focused)
- **Integration**: Includes **MicroG** pre-installed.
- **Benefits**: High privacy, low battery consumption, basic compatibility with apps requiring Google Play Services.
- **Target**: Advanced users and privacy advocates.

## 2. NovaOS-Plus (Full Compatibility)
- **Integration**: Includes a slimmed-down set of **OpenGMS** (prebuilts).
- **Benefits**: Full app compatibility, seamless Google Account sync, Play Store access.
- **Target**: General consumers.

## Implementation Plan
- **Stub GMS**: Create a vendor package `vendor/infynova/gms` containing the necessary prebuilts.
- **Build Flags**: Use `WITH_MICROG := true` or `WITH_GMS := true` in the device tree to toggle between variants.
