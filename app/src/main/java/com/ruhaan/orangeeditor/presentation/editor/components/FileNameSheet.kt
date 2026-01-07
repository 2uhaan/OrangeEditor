package com.ruhaan.orangeeditor.presentation.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ruhaan.orangeeditor.presentation.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileNameSheet(
    modifier: Modifier = Modifier,
    currentFileName: String,
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit,
) {
    var inputText by remember { mutableStateOf(currentFileName) }
    val isValidInput by remember { derivedStateOf { inputText.isNotBlank() } }

    ModalBottomSheet(modifier = modifier, onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.padding(top = 8.dp, end = 16.dp, bottom = 16.dp, start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Rename File",
                    style = Typography.titleSmall,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(inputText)
                            onDismissRequest()
                        },
                        enabled = isValidInput,
                    ) {
                        Text(text = "Save")
                    }
                }
            }

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Enter file name") },
                shape = RoundedCornerShape(16.dp),
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
            )
        }
    }
}