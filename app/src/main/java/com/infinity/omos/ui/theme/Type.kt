package com.infinity.omos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.infinity.omos.R

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        color = white
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 22.sp,
        color = white
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        color = white
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        color = white
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = grey_02
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = grey_02
    )
)

val Typography.alineContents: TextStyle
    get() = TextStyle(
        fontSize = 22.sp,
        lineHeight = 29.04.sp,
        fontFamily = FontFamily(Font(R.font.cafe24oneprettynight)),
        fontWeight = FontWeight(400),
        color = grey_01,
        textAlign = TextAlign.Center
    )

val Typography.lyricsContents: TextStyle
    get() = TextStyle(
        fontSize = 18.sp,
        lineHeight = 26.64.sp,
        fontFamily = FontFamily(Font(R.font.cafe24oneprettynight)),
        fontWeight = FontWeight(400),
        color = grey_03
    )

val Typography.basicContents: TextStyle
    get() = TextStyle(
        fontSize = 16.sp,
        lineHeight = 25.6.sp,
        fontWeight = FontWeight(400),
        color = grey_01
    )