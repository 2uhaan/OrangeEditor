package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kavi.droid.color.picker.ui.pickers.GridColorPicker
import com.ruhaan.orangeeditor.presentation.theme.CanvasOrange
import com.ruhaan.orangeeditor.presentation.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTextSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onTextAdd:
        (
            text: String,
            fontSize: Int,
            fontColor: Color,
            fontWeight: FontWeight,
            fontStyle: FontStyle,
        ) -> Unit,
) {

  // States
  var inputText by remember { mutableStateOf("") }
  var color by remember { mutableStateOf(Color.Black) }
  val fontWeightOptions = listOf(FontWeight.Normal to "Normal", FontWeight.Bold to "Bold")
  val fontStyleOptions = listOf(FontStyle.Normal to "Normal", FontStyle.Italic to "Italic")
  var selectedFontWeight by remember { mutableStateOf(FontWeight.Normal to "Normal") }
  var selectedFontStyle by remember { mutableStateOf(FontStyle.Normal to "Normal") }
  val sliderState = remember { SliderState(value = 80f, valueRange = 10f..150f, steps = 0) }

  val shape = RoundedCornerShape(8.dp)

  // UI
  ModalBottomSheet(modifier = modifier, onDismissRequest = onDismissRequest) {
    Column(
        modifier = Modifier.padding(top = 8.dp, end = 16.dp, bottom = 16.dp, start = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // Header
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Text",
            style = Typography.headlineSmall.copy(),
        )
        Button(
            onClick = {
              onTextAdd(
                  inputText,
                  sliderState.value.toInt(),
                  color,
                  selectedFontWeight.first,
                  selectedFontStyle.first,
              )
              onDismissRequest()
            }
        ) {
          Text("Add text")
        }
      }

      // Text Field
      TextField(
          value = inputText,
          onValueChange = { inputText = it },
          modifier = Modifier.fillMaxWidth(),
          placeholder = { Text(text = "Enter text") },
          shape = RoundedCornerShape(16.dp),
          colors =
              TextFieldDefaults.colors(
                  focusedIndicatorColor = Color.Transparent,
                  unfocusedIndicatorColor = Color.Transparent,
              ),
      )
      Spacer(modifier = modifier.height(4.dp))

      // Font weight
      Text(text = "Font weight", modifier = Modifier.fillMaxWidth(), style = Typography.titleMedium)
      Row(modifier = Modifier.fillMaxWidth()) {
        fontWeightOptions.forEach { fontWeight ->
          RadioButton(
              modifier =
                  Modifier.clip(shape)
                      .then(
                          if (selectedFontWeight == fontWeight)
                              Modifier.border(2.dp, CanvasOrange, shape)
                          else Modifier
                      ),
              text = fontWeight.second,
              style = Typography.bodyMedium.copy(fontWeight = fontWeight.first),
          ) {
            selectedFontWeight = fontWeight
          }
          Spacer(modifier = modifier.width(width = 4.dp))
        }
      }
      Spacer(modifier = modifier.height(4.dp))

      // Font style
      Text(text = "Font style", modifier = Modifier.fillMaxWidth(), style = Typography.titleMedium)
      Row(modifier = Modifier.fillMaxWidth()) {
        fontStyleOptions.forEach { fontStyle ->
          RadioButton(
              modifier =
                  Modifier.clip(shape)
                      .then(
                          if (selectedFontStyle == fontStyle)
                              Modifier.border(2.dp, CanvasOrange, shape)
                          else Modifier
                      ),
              text = fontStyle.second,
              style = Typography.bodyMedium.copy(fontStyle = fontStyle.first),
          ) {
            selectedFontStyle = fontStyle
          }
          Spacer(modifier = modifier.width(width = 4.dp))
        }
      }
      Spacer(modifier = modifier.width(width = 4.dp))

      // Font size
      Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Font size",
            modifier = Modifier.weight(1f),
            style = Typography.titleMedium,
        )
        Text(
            text = "${sliderState.value.toInt()}",
            style = Typography.labelLarge,
        )
      }
      Slider(sliderState, colors = SliderDefaults.colors(activeTickColor = Color.Green))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "${sliderState.valueRange.start.toInt()}px", style = Typography.labelMedium)
        Text(
            text = "${sliderState.valueRange.endInclusive.toInt()}px",
            style = Typography.labelMedium,
        )
      }
      Spacer(modifier = modifier.height(4.dp))

      // Font color
      Text(text = "Font color", modifier = Modifier.fillMaxWidth(), style = Typography.titleMedium)
      GridColorPicker(
          modifier = Modifier.padding(),
          lastSelectedColor = Color.Black,
          onColorSelected = { selectedColor -> color = selectedColor },
      )
    }
  }
}
