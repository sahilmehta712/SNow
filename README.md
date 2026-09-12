# SahilNow

Tiny offline ServiceNow learning reference app for Android 14+.

## Prototype scope
- App name: SahilNow
- Min SDK: 34 (Android 14)
- Target SDK: 35
- Offline/static data only
- No login, API, analytics, database, or external dependencies
- Clean translucent/glass-style UI
- Expandable ServiceNow product areas with important table names

## Build
Open the project in Android Studio and let Gradle sync, then run:

```bash
gradle :app:assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

The repository also contains a GitHub Actions workflow at `.github/workflows/build-apk.yml` that builds and uploads the debug APK as an artifact.

## Data note
The table list is intentionally a compact learning reference, not an exhaustive dump of every ServiceNow application/table. Some products use scoped/custom table names that vary by installed plugins and release.
