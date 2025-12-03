package org.notesapp.project.i18n

data class StringKey(val en: String, val ja: String)

object Strings {
    // Common
    val appName = StringKey("Notes App", "ノートアプリ")
    val save = StringKey("Save", "保存")
    val cancel = StringKey("Cancel", "キャンセル")
    val delete = StringKey("Delete", "削除")
    val edit = StringKey("Edit", "編集")
    val back = StringKey("Back", "戻る")
    val search = StringKey("Search", "検索")
    val searchNotes = StringKey("Search notes...", "ノートを検索...")
    val copy = StringKey("Copy", "コピー")
    val copied = StringKey("Copied!", "コピーしました!")
    val language = StringKey("Language", "言語")
    
    // Login Screen
    val login = StringKey("Login", "ログイン")
    val username = StringKey("Username", "ユーザー名")
    val password = StringKey("Password", "パスワード")
    val loginButton = StringKey("Login", "ログイン")
    val noAccount = StringKey("Don't have an account?", "アカウントをお持ちでないですか？")
    val signUp = StringKey("Sign Up", "新規登録")
    val welcomeBack = StringKey("Welcome Back", "お帰りなさい")
    val loginToContinue = StringKey("Login to continue", "続けるにはログインしてください")
    val loggingIn = StringKey("Logging in...", "ログイン中...")
    val loginFailed = StringKey("Login failed. Please try again.", "ログインに失敗しました。もう一度お試しください。")
    
    // Register Screen
    val register = StringKey("Register", "新規登録")
    val createAccount = StringKey("Create Account", "アカウント作成")
    val joinUs = StringKey("Join us today", "今すぐ参加")
    val confirmPassword = StringKey("Confirm Password", "パスワード確認")
    val registerButton = StringKey("Register", "登録")
    val haveAccount = StringKey("Already have an account?", "既にアカウントをお持ちですか？")
    val signIn = StringKey("Sign In", "サインイン")
    val registering = StringKey("Registering...", "登録中...")
    val registrationFailed = StringKey("Registration failed. Please try again.", "登録に失敗しました。もう一度お試しください。")
    val passwordsDontMatch = StringKey("Passwords don't match", "パスワードが一致しません")
    
    // Notes List Screen
    val myNotes = StringKey("My Notes", "マイノート")
    val allNotes = StringKey("All Notes", "全てのノート")
    val pinnedNotes = StringKey("Pinned", "ピン留め")
    val logout = StringKey("Logout", "ログアウト")
    val addNote = StringKey("Add Note", "ノート追加")
    val noNotes = StringKey("No notes yet", "まだノートがありません")
    val createFirst = StringKey("Create your first note!", "最初のノートを作成しましょう！")
    val loadingNotes = StringKey("Loading notes...", "ノート読込中...")
    val gridView = StringKey("Grid View", "グリッド表示")
    val listView = StringKey("List View", "リスト表示")
    val deleteConfirm = StringKey("Delete this note?", "このノートを削除しますか？")
    val deleteMessage = StringKey("This action cannot be undone.", "この操作は取り消せません。")
    val noteCopied = StringKey("Note copied to clipboard", "ノートをクリップボードにコピーしました")
    
    // Add/Edit Note Screen
    val newNote = StringKey("New Note", "新しいノート")
    val editNote = StringKey("Edit Note", "ノート編集")
    val noteTitle = StringKey("Title", "タイトル")
    val enterTitle = StringKey("Enter title...", "タイトルを入力...")
    val noteContent = StringKey("Content", "内容")
    val enterContent = StringKey("Write your note here...", "ノートの内容を入力...")
    val chooseColor = StringKey("Choose Color", "色を選択")
    val pin = StringKey("Pin", "ピン留め")
    val unpin = StringKey("Unpin", "ピン解除")
    val pinned = StringKey("Pinned", "ピン留め済み")
    val unpinned = StringKey("Unpinned", "ピン解除済み")
    val noteSaved = StringKey("Note saved", "ノートを保存しました")
    val fillTitle = StringKey("Please enter a title", "タイトルを入力してください")
    
    // Colors
    val colorRed = StringKey("Red", "赤")
    val colorOrange = StringKey("Orange", "オレンジ")
    val colorYellow = StringKey("Yellow", "黄色")
    val colorGreen = StringKey("Green", "緑")
    val colorBlue = StringKey("Blue", "青")
    val colorPurple = StringKey("Purple", "紫")
    val colorPink = StringKey("Pink", "ピンク")
    val colorBrown = StringKey("Brown", "茶色")
    val colorGray = StringKey("Gray", "グレー")
    val colorTeal = StringKey("Teal", "ティール")
    val colorDefault = StringKey("Default", "デフォルト")
    
    // Errors
    val errorOccurred = StringKey("An error occurred", "エラーが発生しました")
    val tryAgain = StringKey("Try Again", "再試行")
    val noInternet = StringKey("No internet connection", "インターネット接続がありません")
}
