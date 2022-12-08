package com.deluxe1.chessclock.android.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.android.ui.theme.ChessClockTheme
import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import com.deluxe1.chessclock.models.InputConfig
import org.koin.androidx.compose.getViewModel

@Composable
fun GameDetailsScreen(
    modifier: Modifier = Modifier,
    chessGame: ChessGame?,
    onAction: (String, Int, Int, Long?, Boolean) -> Unit,
    log: Logger,
    inputConfig: InputConfig,
    viewModel: ChessGamePickerViewModel = getViewModel()
) {

    var name by remember { mutableStateOf(chessGame?.name ?: "") }
    var duration by remember { mutableStateOf(if (chessGame != null) (chessGame.time / 1000 / 60).toString() else "") }
    var increment by remember { mutableStateOf(chessGame?.increment?.toString() ?: "") }
    var isChecked by remember { mutableStateOf(true) }
    val editMode by remember { mutableStateOf(chessGame != null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChessClockTheme.colors.background)
            .padding(32.dp)
    ) {
        Text(
            text = if (editMode) "Edit game" else "New game",
            style = MaterialTheme.typography.h4,
            color = ChessClockTheme.colors.onBackground,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.textFieldColors(textColor = ChessClockTheme.colors.onBackground),
            onValueChange = { name = it.take(inputConfig.maxCharsName) },
            label = { Text("Name") }, modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = duration,
            onValueChange = {
                if (it.isDigitsOnly())
                    duration = it.take(inputConfig.maxDigitsDuration)
            },
            colors = TextFieldDefaults.textFieldColors(textColor = ChessClockTheme.colors.onBackground),
            label = { Text("Duration (in minutes)") },
            modifier = Modifier
                .fillMaxWidth(),

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = increment,
            onValueChange = {
                if (it.isDigitsOnly()) increment = it.take(inputConfig.maxDigitsIncrement)
            },
            label = { Text("Increment (in seconds)") },
            colors = TextFieldDefaults.textFieldColors(textColor = ChessClockTheme.colors.onBackground),
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (!editMode) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Save game?", Modifier.padding(bottom = 8.dp))
                Switch(checked = isChecked, onCheckedChange = { isChecked = !isChecked })
            }
        }

        Button(
            enabled = viewModel.isValid(
                name = name,
                durationInMinutes = if (duration.isNotEmpty()) duration.toInt() else 0,
                incrementInSeconds = if (increment.isNotEmpty()) increment.toInt() else -1,
            ),
            onClick = {
                onAction(name, duration.toInt(), increment.toInt(), chessGame?.id, isChecked)
            }) {
            Text(text = if (!editMode) "Start" else "Save")
        }
    }
}
