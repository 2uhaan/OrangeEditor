package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruhaan.orangeeditor.presentation.theme.TextPrimary
import com.ruhaan.orangeeditor.presentation.theme.TextSecondary

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
        text = "Orange Editor",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(text = "What will you design today?", fontSize = 16.sp, color = TextSecondary)

    Spacer(modifier = Modifier.height(30.dp))

    Text(
        text = "Create New",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
    )
  }
}
