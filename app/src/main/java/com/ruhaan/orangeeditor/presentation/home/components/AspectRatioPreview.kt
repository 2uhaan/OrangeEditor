package com.ruhaan.orangeeditor.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange

//These are the Orange Shapes Icons to represent screen size
@Composable
fun AspectRatioPreview(
    aspectRatio: String,
    modifier: Modifier = Modifier
) {
    //The Shape
    Box(
        modifier = modifier
            .size(calculateDpSize(aspectRatio))
            .background(CanvasOrange),
        contentAlignment = Alignment.Center
    ) {
        //The Number Inside
        Text(
            text = aspectRatio,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


//Helper for shape design
private fun calculateDpSize(aspectRatio: String): DpSize {
    val base = 80f

    return when (aspectRatio) {
        "9:16" -> DpSize((base * 0.7f).dp, (base * 1.42f).dp) // Balanced Story
        "4:5"  -> DpSize((base * 0.9f).dp, (base * 1.125f).dp) // Balanced Portrait
        "16:9" -> DpSize((base * 1.4f).dp, (base * 0.78f).dp)  // Balanced Wide
        "1:1"  -> DpSize(base.dp, base.dp)
        else   -> DpSize(base.dp, base.dp)
    }
}