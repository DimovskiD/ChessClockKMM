package com.deluxe1.chessclock

import com.deluxe1.chessclock.models.CallbackViewModel
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import co.touchlab.kermit.Logger

@Suppress("Unused") // Members are called from Swift
class ChessGamePickerCallbackViewModel(
    useCases: UseCases,
    log: Logger
) : CallbackViewModel() {

    override val viewModel = ChessGamePickerViewModel(useCases, log)

    val chessGames = viewModel.chessGameState.asCallbacks()
    val inputConfig = viewModel.inputConfig

    fun upsertChessGame(
        name: String,
        durationInMinutes: Int,
        incrementInSeconds: Int,
        id: Long = -1L
    ) {
        viewModel.insertChessGame(name, durationInMinutes, incrementInSeconds, id)
    }

    fun isValid(name: String, durationInMinutes: Int, incrementInSeconds: Int) =
        viewModel.isValid(name, durationInMinutes, incrementInSeconds)
}
