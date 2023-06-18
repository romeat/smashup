package com.romeat.smashup.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.romeat.smashup.R

// Set of Material typography styles to start with
val customFont = FontFamily(
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val Typography = Typography(
    defaultFontFamily = customFont,

    h5 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.25.sp,
        fontSize = 22.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.25.sp,
        fontSize = 19.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.25.sp,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp,
        fontSize = 13.sp
    ),
    overline = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        letterSpacing = 0.3.sp
    )

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)