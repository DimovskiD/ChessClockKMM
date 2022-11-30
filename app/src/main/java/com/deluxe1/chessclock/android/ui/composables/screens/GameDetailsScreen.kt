package com.deluxe1.chessclock.android.ui.composables.screens

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun GameDetailsScreen(
    modifier: Modifier = Modifier,
    chessGameId: Long? = null,
    log: Logger,
    viewModel: ChessGamePickerViewModel = getViewModel()
) {
    val chessGame by viewModel.getGameById(chessGameId).collectAsState(initial = null)

    var name by remember { mutableStateOf(chessGame?.name ?: "") }
    var duration by remember { mutableStateOf(if (chessGame != null) (chessGame!!.time / 1000 / 60).toString() else "") }
    var increment by remember { mutableStateOf(chessGame?.increment?.toString() ?: "") }
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = if (chessGame != null) "Edit game" else "New game",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onValueChange = { name = it },
            label = { Text("Name") }, modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = duration, onValueChange = { duration = it }, label = { Text("Duration") },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = increment, onValueChange = { increment = it }, label = { Text("Increment") },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (chessGame == null) {
            Text(text = "Save game?", Modifier.padding(bottom = 8.dp))
            Switch(checked = false, onCheckedChange = {})
        }

        Button(onClick = {
            if (viewModel.isValid(name, duration.toInt(), increment.toInt()))
                viewModel.insertChessGame(
                    name = name,
                    durationInMinutes = duration.toInt(),
                    incrementInSeconds = increment.toInt(),
                    id = -1L
                )
        }) {

        }
    }
}