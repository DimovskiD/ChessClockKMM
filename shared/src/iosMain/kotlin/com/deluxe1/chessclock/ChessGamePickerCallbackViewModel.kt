package com.deluxe1.chessclock

import com.deluxe1.chessclock.models.CallbackViewModel
import com.deluxe1.chessclock.models.ChessGamePickerViewModel
import co.touchlab.kermit.Logger
import com.deluxe1.chessclock.db.ChessGame

@Suppress("Unused") // Members are called from Swift
class ChessGamePickerCallbackViewModel(
    useCases: UseCases,
    log: Logger
) : CallbackViewModel() {

    override val viewModel = ChessGamePickerViewModel(useCases, log)

    val chessGames = viewModel.chessGameState.asCallbacks()
    val inputConfig = viewModel.inputConfig

    fun upsertChessGame(chessGame: ChessGame) {
        viewModel.insertChessGame(chessGame)
    }

    fun getChessGame(name: String, durationInMinutes: Int, incrementInSeconds: Int, id: Long = -1L) : ChessGame {
       return viewModel.createChessGame(name, durationInMinutes, incrementInSeconds, id)
    }
}
