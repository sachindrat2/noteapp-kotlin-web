# 🚀 Quick Start Guide

## Running the Notes App

### Option 1: Android
```bash
./gradlew :composeApp:installDebug
```
Or click the "Run" button in Android Studio with an Android device/emulator selected.

### Option 2: Web Browser (Recommended for Quick Testing)
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```
The app will automatically open in your default browser at http://localhost:8080

### Option 3: Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### Option 4: iOS (Mac only)
Open `iosApp/iosApp.xcodeproj` in Xcode and run.

## What to Expect

✨ **Beautiful UI Features:**
- Sample notes with different colors
- Smooth spring animations
- Grid/List view toggle
- Search functionality
- Pin/Unpin notes
- Add/Edit/Delete notes
- Color picker with 11 themes
- Responsive design

🎨 **Try These Interactions:**
1. Click the **+** button to create a new note
2. Tap on any note to edit it
3. Click the color palette icon to change note colors
4. Use the search icon to filter notes
5. Toggle between grid and list views
6. Pin important notes to keep them at the top
7. Watch the smooth animations throughout!

## Troubleshooting

**Android:** Make sure you have an Android device connected or emulator running.

**Web:** If port 8080 is busy, the Gradle task will use an available port.

**Desktop:** Requires JDK 11 or higher.

**iOS:** Requires macOS with Xcode 15+.
