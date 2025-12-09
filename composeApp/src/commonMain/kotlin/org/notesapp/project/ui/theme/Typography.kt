package org.notesapp.project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


expect val japaneseFontFamily: FontFamily

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = japaneseFontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
