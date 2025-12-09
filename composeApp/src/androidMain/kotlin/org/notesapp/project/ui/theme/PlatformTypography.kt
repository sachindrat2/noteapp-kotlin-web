package org.notesapp.project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.notesapp.project.R
actual val japaneseFontFamily = FontFamily(
    Font(resId = R.font.notosansjp_regular, weight = FontWeight.Normal),
    Font(resId = R.font.notosansjp_bold, weight = FontWeight.Bold)
)

val PlatformTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = japaneseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = japaneseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = japaneseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    )
    // ...add other styles as needed
)
