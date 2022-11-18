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

    fun onCreateNewGameClicked() {
        viewModel.onCreateNewGameClicked()
    }
}
