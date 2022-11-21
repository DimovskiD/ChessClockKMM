package com.deluxe1.chessclock.android.ui.composables.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.deluxe1.chessclock.android.ui.composables.ChessGamePickerScreenContent
import com.deluxe1.chessclock.db.ChessGame
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import co.touchlab.kermit.Logger

@Composable
fun ChessGamePickerScreen(
    viewModel: ChessGamePickerViewModel,
    onNavigateToChessGame: (ChessGame) -> Unit,
    log: Logger
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareDogsFlow = remember(viewModel.chessGameState, lifecycleOwner) {
        viewModel.chessGameState.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val chessGameState by lifecycleAwareDogsFlow.collectAsState(viewModel.chessGameState.value)

    ChessGamePickerScreenContent(
        chessGameState = chessGameState,
        onSuccess = { data -> log.v { "View updating with ${data.size} games" } },
        onError = { exception -> log.e { "Displaying error: $exception" } },
        onOpen = { onNavigateToChessGame(it) },
        onCreateNewClicked = {
            // viewModel.onCreateNewGameClicked(it)
        }
    )
}
