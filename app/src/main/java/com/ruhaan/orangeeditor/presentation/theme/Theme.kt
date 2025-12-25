package com.ruhaan.orangeeditor.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
    lightColorScheme(
        primary = CanvasOrange,
        onPrimary = Color.Black,
        background = BackgroundLight,
    )

@Composable
fun OrangeEditorTheme(content: @Composable () -> Unit) {

  MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
