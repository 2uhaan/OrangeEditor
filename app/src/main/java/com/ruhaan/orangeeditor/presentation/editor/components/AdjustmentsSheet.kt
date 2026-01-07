package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.domain.model.layer.Adjustment
import com.ruhaan.orangeeditor.domain.model.layer.NeutralAdjustment
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange
import com.ruhaan.orangeeditor.presentation.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustmentsSheet(
  modifier: Modifier = Modifier,
  adjustment: Adjustment,
  onAdjustmentsConfirm: (Adjustment) -> Unit,
  onDismissRequest: () -> Unit,
) {

  val saturation = remember { SliderState(value = adjustment.saturation, valueRange = 0f..2f) }
  val brightness = remember {
    SliderState(value = adjustment.brightness, valueRange = -100f..100f)
  }
  val contrast = remember { SliderState(value = adjustment.contrast, valueRange = 0.5f..1.5f) }
  val exposure = remember { SliderState(value = adjustment.exposure, valueRange = -1f..1f) }
  val temperature = remember {
    SliderState(value = adjustment.temperature, valueRange = -100f..100f)
  }
  val tint = remember { SliderState(value = adjustment.tint, valueRange = -100f..100f) }
  val hue = remember { SliderState(value = adjustment.hue, valueRange = -180f..180f) }

  ModalBottomSheet(modifier = modifier, onDismissRequest = onDismissRequest) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = "Adjustments",
            style = Typography.headlineSmall,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          OutlinedButton(
              onClick = {
                saturation.value = NeutralAdjustment.saturation
                brightness.value = NeutralAdjustment.brightness
                contrast.value = NeutralAdjustment.contrast
                exposure.value = NeutralAdjustment.exposure
                temperature.value = NeutralAdjustment.temperature
                tint.value = NeutralAdjustment.tint
                hue.value = NeutralAdjustment.hue
              },
              border = BorderStroke(width = 2.dp, color = CanvasOrange),
          ) {
            Text(
                text = "Reset",
            )
          }
          Button(
              onClick = {
                val adjustment =
                    Adjustment(
                        saturation = saturation.value,
                        brightness = brightness.value,
                        contrast = contrast.value,
                        exposure = exposure.value,
                        temperature = temperature.value,
                        tint = tint.value,
                        hue = hue.value,
                    )
                onAdjustmentsConfirm(adjustment)
                onDismissRequest()
              }
          ) {
            Text(text = "Apply")
          }
        }
      }

      AdjustmentSlider("Saturation", saturation, "%.2f")
      AdjustmentSlider("Brightness", brightness, "%.0f")
      AdjustmentSlider("Contrast", contrast, "%.2f")
      AdjustmentSlider("Exposure", exposure, "%.2f")
      AdjustmentSlider("Temperature", temperature, "%.0f")
      AdjustmentSlider("Tint", tint, "%.0f")
      AdjustmentSlider("Hue", hue, "%.0f")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdjustmentSlider(label: String, sliderState: SliderState, valueFormat: String) {
  Column {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Text(text = label, modifier = Modifier.weight(1f), style = Typography.titleMedium)
      Text(text = valueFormat.format(sliderState.value), style = Typography.labelLarge)
    }
    Slider(sliderState)
    Spacer(modifier = Modifier.height(4.dp))
  }
}
