package co.touchlab.kampkit

import co.touchlab.kampkit.data.Player
import co.touchlab.kampkit.models.CallbackViewModel
import co.touchlab.kampkit.models.ChessGameViewModel
import co.touchlab.kermit.Logger

@Suppress("Unused") // Members are called from Swift
class ChessGameCallbackViewModel(
    gameId: Long,
    useCases: UseCases,
    log: Logger
) : CallbackViewModel() {

    override val viewModel = ChessGameViewModel(gameId, useCases, log)

    val chessGameState = viewModel.chessGameState.asCallbacks()

    fun playPauseClicked() {
        viewModel.playPauseClicked()
    }

    fun onRestartClicked() {
        viewModel.onRestartClicked()
    }

    fun switchPlayer() {
        viewModel.switchPlayer()
    }

    fun getWinner(): Player? {
        return viewModel.getWinner()
    }
}
