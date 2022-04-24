package com.example.mobiletestrakesh.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.mobiletestrakesh.R


private val Inter = FontFamily(
    Font(R.font.inter_black, FontWeight.Black),
    Font(R.font.inter_bold,FontWeight.Bold),
    Font(R.font.inter_extrabold,FontWeight.ExtraBold),
    Font(R.font.inter_extralight,FontWeight.ExtraLight),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_regular,FontWeight.Normal),
    Font(R.font.inter_semibold,FontWeight.SemiBold),
    Font(R.font.inter_thin,FontWeight.Thin)
)

// Set of Material typography styles to start with
val Typography = Typography(

    defaultFontFamily = Inter,

    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    h2 = TextStyle(
//        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h3 = TextStyle(
//        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textDecoration = TextDecoration.Underline
    ),
    h4 = TextStyle(
//        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 20.sp,
        color = DialogFilter
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
//        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
//        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
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