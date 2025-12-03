package org.notesapp.project.utils

enum class Language {
    ENGLISH,
    JAPANESE
}

data class LocalizedStrings(
    val welcomeBack: String,
    val loginSubtitle: String,
    val email: String,
    val password: String,
    val forgotPassword: String,
    val login: String,
    val or: String,
    val googleSignIn: String,
    val appleSignIn: String,
    val noAccount: String,
    val signUp: String,
    val emailRequired: String,
    val invalidEmail: String,
    val passwordRequired: String,
    val passwordTooShort: String
)

object Strings {
    private val englishStrings = LocalizedStrings(
        welcomeBack = "Welcome Back",
        loginSubtitle = "Sign in to continue to your notes",
        email = "Email",
        password = "Password",
        forgotPassword = "Forgot Password?",
        login = "Login",
        or = "OR",
        googleSignIn = "Continue with Google",
        appleSignIn = "Continue with Apple",
        noAccount = "Don't have an account?",
        signUp = "Sign Up",
        emailRequired = "Email is required",
        invalidEmail = "Please enter a valid email",
        passwordRequired = "Password is required",
        passwordTooShort = "Password must be at least 6 characters"
    )
    
    private val japaneseStrings = LocalizedStrings(
        welcomeBack = "おかえりなさい",
        loginSubtitle = "ノートに続けるためにサインインしてください",
        email = "メールアドレス",
        password = "パスワード",
        forgotPassword = "パスワードをお忘れですか？",
        login = "ログイン",
        or = "または",
        googleSignIn = "Googleで続ける",
        appleSignIn = "Appleで続ける",
        noAccount = "アカウントをお持ちではありませんか？",
        signUp = "新規登録",
        emailRequired = "メールアドレスは必須です",
        invalidEmail = "有効なメールアドレスを入力してください",
        passwordRequired = "パスワードは必須です",
        passwordTooShort = "パスワードは6文字以上である必要があります"
    )
    
    fun getStrings(language: Language): LocalizedStrings {
        return when (language) {
            Language.ENGLISH -> englishStrings
            Language.JAPANESE -> japaneseStrings
        }
    }
}

data class LocalizedRegisterStrings(
    val createAccount: String,
    val registerSubtitle: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val register: String,
    val or: String,
    val googleSignUp: String,
    val appleSignUp: String,
    val alreadyHaveAccount: String,
    val login: String,
    val usernameRequired: String,
    val passwordRequired: String,
    val passwordTooShort: String,
    val confirmPasswordRequired: String,
    val passwordsDoNotMatch: String,
    val registrationFailed: String
)

object RegisterStrings {
    private val englishStrings = LocalizedRegisterStrings(
        createAccount = "Create Account",
        registerSubtitle = "Sign up to start organizing your notes",
        username = "Username",
        password = "Password",
        confirmPassword = "Confirm Password",
        register = "Register",
        or = "OR",
        googleSignUp = "Sign up with Google",
        appleSignUp = "Sign up with Apple",
        alreadyHaveAccount = "Already have an account?",
        login = "Login",
        usernameRequired = "Username is required",
        passwordRequired = "Password is required",
        passwordTooShort = "Password must be at least 6 characters",
        confirmPasswordRequired = "Please confirm your password",
        passwordsDoNotMatch = "Passwords do not match",
        registrationFailed = "Registration failed. Please try again."
    )
    
    private val japaneseStrings = LocalizedRegisterStrings(
        createAccount = "アカウント作成",
        registerSubtitle = "ノートを整理するために新規登録してください",
        username = "ユーザー名",
        password = "パスワード",
        confirmPassword = "パスワード確認",
        register = "登録",
        or = "または",
        googleSignUp = "Googleで新規登録",
        appleSignUp = "Appleで新規登録",
        alreadyHaveAccount = "すでにアカウントをお持ちですか？",
        login = "ログイン",
        usernameRequired = "ユーザー名は必須です",
        passwordRequired = "パスワードは必須です",
        passwordTooShort = "パスワードは6文字以上である必要があります",
        confirmPasswordRequired = "パスワードを確認してください",
        passwordsDoNotMatch = "パスワードが一致しません",
        registrationFailed = "登録に失敗しました。もう一度お試しください。"
    )
    
    fun getStrings(language: Language): LocalizedRegisterStrings {
        return when (language) {
            Language.ENGLISH -> englishStrings
            Language.JAPANESE -> japaneseStrings
        }
    }
}
