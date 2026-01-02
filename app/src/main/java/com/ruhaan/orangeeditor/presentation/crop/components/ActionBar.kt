package com.ruhaan.orangeeditor.presentation.crop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ruhaan.orangeeditor.R
import com.ruhaan.orangeeditor.presentation.components.LargeIconButton

@Composable
fun ActionBar(
    modifier: Modifier = Modifier,
    onVerticalFlip: () -> Unit,
    onHorizontalFlip: () -> Unit,
    onAntiClockWiseRotate: () -> Unit,
    onClockWiseRotate: () -> Unit,
) {

  Row(modifier = modifier.fillMaxWidth(.8f), horizontalArrangement = Arrangement.SpaceBetween) {
    LargeIconButton(
        iconId = R.drawable.ic_vertical_flip,
        contentDescription = "vertical flip",
        onClick = onVerticalFlip,
    )
    LargeIconButton(
        iconId = R.drawable.ic_horizontal_flip,
        contentDescription = "horizontal flip",
        onClick = onHorizontalFlip,
    )
    LargeIconButton(
        iconId = R.drawable.ic_rotate_anti_clock_wise,
        contentDescription = "Rotate anti clock wise",
        onClick = onAntiClockWiseRotate,
    )
    LargeIconButton(
        iconId = R.drawable.ic_rotate_clock_wise,
        contentDescription = "Rotate clock wise",
        onClick = onClockWiseRotate,
    )
  }
}
