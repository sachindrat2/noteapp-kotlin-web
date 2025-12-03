# 📝 Notes App - Kotlin Multiplatform

A beautiful, pixel-perfect notes application built with Kotlin Multiplatform Compose for Android, iOS, and Web with a single codebase.

## ✨ Features

### 🎨 Beautiful UI/UX
- **Material 3 Design** - Modern, clean interface following Material Design guidelines
- **Smooth Animations** - Spring-based animations and transitions throughout the app
- **Dark/Light Theme** - Automatic theme support based on system preferences
- **Responsive Layout** - Adapts perfectly to different screen sizes

### 📱 Core Features
- ✅ Create, edit, and delete notes
- ✅ Pin important notes to the top
- ✅ Search notes by title or content
- ✅ 11 beautiful color themes for notes
- ✅ Grid and list view toggle
- ✅ Staggered grid layout with animated item placement
- ✅ Real-time timestamp display (Just now, 5m ago, etc.)
- ✅ Empty state with animated placeholder

### 🎬 Animations
- **Slide & Fade** transitions between screens
- **Spring animations** for interactive elements
- **Staggered grid** animations
- **Scale animations** on card interactions
- **Color picker** with smooth expand/collapse
- **Save confirmation** animation
- **Infinite pulse** animation on empty state
- **Animated visibility** for search and actions

### 🌈 Color Themes
- Default (White)
- Red
- Orange
- Yellow
- Green
- Teal
- Blue
- Purple
- Pink
- Brown
- Gray

## 🏗️ Architecture

### Project Structure
```
composeApp/
├── src/
│   ├── commonMain/
│   │   └── kotlin/org/notesapp/project/
│   │       ├── model/
│   │       │   └── Note.kt              # Data models
│   │       ├── viewmodel/
│   │       │   └── NotesViewModel.kt    # State management
│   │       ├── ui/
│   │       │   ├── screens/
│   │       │   │   ├── NotesListScreen.kt     # Main screen
│   │       │   │   └── AddEditNoteScreen.kt   # Editor screen
│   │       │   └── theme/
│   │       │       ├── Theme.kt         # App theme
│   │       │       └── Typography.kt    # Typography system
│   │       └── App.kt                   # Main app entry
│   ├── androidMain/
│   ├── iosMain/
│   └── jsMain/
```

### Design Patterns
- **MVVM Architecture** - Separation of concerns with ViewModel
- **Unidirectional Data Flow** - State flows down, events flow up
- **Composition over Inheritance** - Composable functions for UI
- **Single Source of Truth** - ViewModel holds the app state

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK** 11 or higher
- **Xcode** 15+ (for iOS development)
- **Node.js** 18+ (for web development)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Notesapp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the Notesapp folder

### Running on Different Platforms

#### Android
```bash
./gradlew :composeApp:assembleDebug
# Or run from Android Studio
```

#### iOS
```bash
# Open iosApp/iosApp.xcodeproj in Xcode
# Select target device and run
```

#### Web (Browser)
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
# App will open at http://localhost:8080
```

#### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

## 🎯 Upcoming Features (API Integration)

The app is designed to easily integrate with backend APIs:

### Planned API Features
- [ ] User authentication (Login/Register)
- [ ] Cloud sync across devices
- [ ] Real-time collaboration
- [ ] Note sharing
- [ ] Backup and restore
- [ ] Rich text formatting
- [ ] Image attachments
- [ ] Voice notes
- [ ] Categories and tags
- [ ] Export to PDF/Markdown

### API Integration Points

The app is structured to easily add API integration:

```kotlin
// Future API service structure
interface NotesApiService {
    suspend fun getNotes(): List<Note>
    suspend fun createNote(note: Note): Note
    suspend fun updateNote(note: Note): Note
    suspend fun deleteNote(id: String): Boolean
    suspend fun syncNotes(): List<Note>
}
```

## 🎨 Design Philosophy

### Pixel Perfect Design
- Consistent spacing using 4dp grid system
- Carefully crafted animations with spring physics
- Color harmony using Material You color system
- Responsive typography scale

### Animation Principles
- **Duration**: 300-400ms for most transitions
- **Easing**: Spring animations for organic feel
- **Purpose**: Every animation serves a functional purpose
- **Performance**: Optimized for 60fps on all platforms

### Accessibility
- High contrast ratios
- Touch targets minimum 48dp
- Screen reader support (planned)
- Keyboard navigation (planned)

## 🛠️ Tech Stack

- **Kotlin** 2.2.20
- **Compose Multiplatform** 1.9.1
- **Material 3** - Modern design system
- **Lifecycle ViewModel** - State management
- **Coroutines** - Async operations (planned)
- **Ktor** - HTTP client (planned for API)
- **kotlinx.serialization** - JSON serialization (planned)

## 📊 Performance

- Lazy loading with `LazyVerticalStaggeredGrid`
- Efficient state management with `mutableStateOf`
- Optimized recomposition with `remember` and `derivedStateOf`
- Smooth 60fps animations

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Created with ❤️ using Kotlin Multiplatform

---

**Note**: This is a demonstration app showcasing pixel-perfect design and smooth animations in Kotlin Multiplatform. API integration will be added in the next phase.
