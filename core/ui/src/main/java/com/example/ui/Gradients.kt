package com.example.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


object AppGradient {
    val GradientBegin = Color(0xffF3F3F6)
    val GradientEnd = Color(0xffFAFAFA)

    val PrimaryGradient = Brush.linearGradient(
        colors = listOf(GradientBegin, GradientEnd)
    )
}