# Notes App - Kotlin MultiplatformThis is a Kotlin Multiplatform project targeting Android, iOS, Web.



A beautiful, responsive notes application built with Kotlin Multiplatform and Compose Multiplatform, supporting Android, iOS, Web, and Desktop platforms with multilingual support.* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.

  It contains several subfolders:

## 🌟 Features  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.

  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.

### ✨ Core Features    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,

- **Create, Read, Update, Delete** notes with rich text support    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.

- **Color Coding** - 11 beautiful color themes for note organization    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)

- **Pin Important Notes** - Keep important notes at the top    folder is the appropriate location.

- **Search** - Quickly find notes with real-time search

- **Grid/List Views** - Toggle between grid and list layouts* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,

- **Copy to Clipboard** - Copy note content with one click (Web/Desktop)  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.



### 🌍 Multilingual Support### Build and Run Android Application

- **English** and **日本語 (Japanese)** translations

- Language selector accessible from all screensTo build and run the development version of the Android app, use the run configuration from the run widget

- Persistent language preference across sessionsin your IDE’s toolbar or build it directly from the terminal:

- Comprehensive translations for all UI elements- on macOS/Linux

  ```shell

### 📱 Responsive Design  ./gradlew :composeApp:assembleDebug

- **Adaptive Layouts** for all screen sizes  ```

- **Breakpoints**: - on Windows

  - Compact (<600dp) - Mobile phones  ```shell

  - Medium (600-840dp) - Tablets and foldables  .\gradlew.bat :composeApp:assembleDebug

  - Expanded (>840dp) - Desktop and large tablets  ```

- **Dynamic Grid Columns**: 2/3/4 based on screen width

- **Optimized Touch Targets** for mobile usability### Build and Run Web Application



### 🌐 Web FeaturesTo build and run the development version of the web app, use the run configuration from the run widget

- **Deep Linking** - Direct URLs for all routesin your IDE's toolbar or run it directly from the terminal:

- **Browser Navigation** - Full support for back/forward buttons- for the Wasm target (faster, modern browsers):

- **Routes**:  - on macOS/Linux

  - `/login` - Login page    ```shell

  - `/register` - Registration page    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun

  - `/notes` - Notes list    ```

  - `/notes/add` - Create new note  - on Windows

  - `/notes/edit/:id` - Edit existing note    ```shell

- **Copy to Clipboard** - Native web clipboard API integration    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun

    ```

### 🔐 Authentication- for the JS target (slower, supports older browsers):

- User registration and login  - on macOS/Linux

- JWT token-based authentication    ```shell

- Persistent sessions with auto-login    ./gradlew :composeApp:jsBrowserDevelopmentRun

- Secure token storage (LocalStorage/Preferences/SharedPreferences)    ```

  - on Windows

### 🎨 Design    ```shell

- **Material Design 3** - Modern, beautiful UI    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun

- **Smooth Animations** - Spring animations and transitions    ```

- **Color Themes**: Red, Orange, Yellow, Green, Blue, Purple, Pink, Brown, Gray, Teal, Default

- **Dark/Light Mode Ready** - Respects system preferences### Build and Run iOS Application



## 🚀 Quick StartTo build and run the development version of the iOS app, use the run configuration from the run widget

in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Build Commands

---

#### Web Development Server

```powershellLearn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),

cd Notesapp[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),

.\gradlew.bat :composeApp:jsBrowserDevelopmentRun[Kotlin/Wasm](https://kotl.in/wasm/)…

```

Access at: http://localhost:8080We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).

If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).
#### Desktop Application
```powershell
cd Notesapp
.\gradlew.bat :composeApp:run
```

#### Android Install
```powershell
cd Notesapp
.\gradlew.bat :composeApp:installDebug
```

## 🌐 API Integration

- **Backend**: https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net
- **Authentication**: JWT Bearer tokens
- **Endpoints**: Login, Register, Notes CRUD

## 📦 Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for comprehensive deployment instructions to Azure Static Web Apps.

## 📚 Documentation

- **Kotlin Multiplatform**: https://kotlinlang.org/docs/multiplatform.html
- **Compose Multiplatform**: https://www.jetbrains.com/lp/compose-multiplatform/
- **Material Design 3**: https://m3.material.io/

---

**Built with ❤️ using Kotlin Multiplatform**
