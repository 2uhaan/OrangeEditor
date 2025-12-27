package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruhaan.orangeeditor.domain.model.CanvasFormat
import com.ruhaan.orangeeditor.presentation.theme.CardBackground
import com.ruhaan.orangeeditor.presentation.theme.TextPrimary

@Composable
fun CanvasFormatCard(
    modifier: Modifier = Modifier,
    canvasFormat: CanvasFormat,  // Data to display
    onClick: () -> Unit  // Click callback
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable { onClick() },  // Make card clickable
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // The orange icons are used here
            AspectRatioPreview(
                aspectRatio = canvasFormat.aspectRatio
            )

            Spacer(modifier = Modifier.height(12.dp))

            // screen size - written below the icons (Story or Post...)
            Text(
                text = canvasFormat.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
