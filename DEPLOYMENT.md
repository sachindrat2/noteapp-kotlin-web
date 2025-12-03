# Notes App - Deployment Guide

## Overview
This Kotlin Multiplatform application is designed for deployment across Android, iOS, Web, and Desktop platforms with full multilingual support (English/Japanese) and responsive design.

## Features Implemented

### ✅ Multilingual Support (i18n)
- English and Japanese languages
- Language selector on all screens
- Persistent language preference
- Location: `i18n/` package

### ✅ Responsive Design
- Adaptive layouts for mobile, tablet, and desktop
- Breakpoints: Compact (<600dp), Medium (600-840dp), Expanded (>840dp)
- Grid columns adapt: 2/3/4 based on screen size
- Location: `ui/responsive/` package

### ✅ Web Routing
- Deep linking support for web
- Routes: /login, /register, /notes, /notes/add, /notes/edit/:id
- Browser back/forward navigation
- Location: `navigation/` package

### ✅ Clipboard Support
- Copy note content on web platform
- Platform-specific implementations (JS, JVM, Android)
- Location: `utils/ClipboardManager`

### ✅ Azure API Integration
- Backend: https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net
- Authentication with Bearer tokens
- CRUD operations for notes
- Auto-login with persisted tokens

## Build Commands

### Web (Production)
```powershell
cd Notesapp
.\gradlew.bat :composeApp:jsBrowserProductionWebpack
```
Output: `composeApp/build/dist/js/productionExecutable/`

### Web (Development)
```powershell
cd Notesapp
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```
Access at: http://localhost:8080

### Desktop
```powershell
cd Notesapp
.\gradlew.bat :composeApp:run
```

### Android
```powershell
cd Notesapp
.\gradlew.bat :composeApp:installDebug
```

## Azure Static Web Apps Deployment

### Prerequisites
1. Azure account with Static Web Apps resource
2. GitHub repository connected to Azure Static Web Apps
3. Azure Static Web Apps API token (from Azure Portal)

### Deployment Steps

#### Option 1: Automatic GitHub Actions
1. Push code to GitHub repository
2. GitHub Actions workflow will automatically:
   - Build the production web bundle
   - Deploy to Azure Static Web Apps
3. Configuration file: `.github/workflows/azure-static-web-apps.yml`

#### Option 2: Manual Azure CLI
```powershell
# Build production bundle
.\gradlew.bat :composeApp:jsBrowserProductionWebpack

# Deploy using Azure CLI
az staticwebapp deploy `
  --name <your-static-web-app-name> `
  --resource-group <your-resource-group> `
  --app-location "./composeApp/build/dist/js/productionExecutable" `
  --token <your-deployment-token>
```

#### Option 3: Azure Portal Upload
1. Build: `.\gradlew.bat :composeApp:jsBrowserProductionWebpack`
2. Navigate to Azure Portal → Your Static Web App
3. Go to "Deploy" → "Upload"
4. Upload contents of `composeApp/build/dist/js/productionExecutable/`

### Configuration Files

**`staticwebapp.config.json`** - Azure Static Web Apps configuration
- Defines routing rules for SPA
- Sets up navigation fallback to index.html
- Configures MIME types for .wasm and .mjs files
- Location: Root directory

**`.github/workflows/azure-static-web-apps.yml`** - GitHub Actions workflow
- Auto-builds on push to main branch
- Deploys to Azure Static Web Apps
- Requires secret: `AZURE_STATIC_WEB_APPS_API_TOKEN`

### Environment Variables (Optional)
Create `gradle.properties` for different API endpoints:

```properties
# Development
apiBaseUrl=http://localhost:5000

# Production
apiBaseUrl=https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net
```

Then update `NotesApiService.kt` to read from build config.

## Testing Checklist

### Desktop
- [x] Launch app
- [x] Test login/register
- [x] Create/edit/delete notes
- [x] Test multilingual support
- [x] Test clipboard copy

### Web
- [ ] Test on Chrome (1920x1080, 768x1024, 375x667)
- [ ] Test on Firefox
- [ ] Test URL routing (/login, /notes, /notes/add, /notes/edit/:id)
- [ ] Test browser back/forward buttons
- [ ] Test language switching
- [ ] Test clipboard copy
- [ ] Test responsive breakpoints

### Android
- [ ] Install on emulator
- [ ] Test on different screen sizes
- [ ] Test multilingual support
- [ ] Verify authentication persistence

## Architecture

### Modules
- `commonMain` - Shared code across all platforms
- `jsMain` - Web-specific implementations
- `jvmMain` - Desktop-specific implementations
- `androidMain` - Android-specific implementations
- `iosMain` - iOS-specific implementations (future)

### Key Components
1. **i18n System** - `LocalizationManager` with `StringKey` translations
2. **Navigation** - `NavigationController` with web routing support
3. **Responsive** - `ResponsiveInfo` for adaptive layouts
4. **Clipboard** - Expect/actual pattern for platform-specific clipboard
5. **Storage** - `AuthStorage` for tokens and preferences

### API Integration
- **Base URL**: https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net
- **Authentication**: POST /token (form-urlencoded)
- **Registration**: POST /register (JSON)
- **Notes**: GET/POST /notes, PUT/DELETE /notes/{id}
- **Auth Header**: `Authorization: Bearer <token>`

## Performance Optimization

### Production Build
- Minified JavaScript
- Optimized WASM module
- Tree-shaking for unused code
- Skiko WASM for Compose rendering

### Web Optimizations
- Lazy loading of routes
- Efficient re-compositions with `remember` and `derivedStateOf`
- Material 3 animations with hardware acceleration

## Troubleshooting

### Build Issues
```powershell
# Clean build
.\gradlew.bat clean

# Rebuild with logs
.\gradlew.bat :composeApp:jsBrowserProductionWebpack --info
```

### Port Already in Use
```powershell
# Kill Java processes
Get-Process | Where-Object {$_.ProcessName -eq "java"} | ForEach-Object { Stop-Process -Id $_.Id -Force }
```

### WASM Loading Issues
- Ensure `.wasm` MIME type is configured in `staticwebapp.config.json`
- Check browser console for CORS errors
- Verify all static assets are deployed

## Next Steps

1. **iOS Development** - Requires Mac with Xcode
2. **Performance Monitoring** - Add analytics to track usage
3. **Offline Support** - Implement local caching with SQLDelight
4. **Push Notifications** - Add real-time sync
5. **Themes** - Expand beyond Material 3 default

## Support
For issues or questions, refer to:
- Compose Multiplatform Docs: https://www.jetbrains.com/lp/compose-multiplatform/
- Kotlin Multiplatform: https://kotlinlang.org/docs/multiplatform.html
- Azure Static Web Apps: https://docs.microsoft.com/azure/static-web-apps/
